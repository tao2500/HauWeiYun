package com.bdc.reservationService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdc.reservationService.entity.Numberball;
import com.bdc.reservationService.entity.Reservationinformation;
import com.bdc.reservationService.mapper.NumberballMapper;
import com.bdc.reservationService.mapper.ReservationinformationMapper;
import com.bdc.reservationService.service.IReservationinformationService;
import com.bdc.reservationService.util.JwtUtils;
import com.bdc.reservationService.util.RedisUtils;
import com.bdc.userService.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bc
 * @since 2023-05-16
 */
@Service
public class  ReservationinformationServiceImpl extends ServiceImpl<ReservationinformationMapper, Reservationinformation> implements IReservationinformationService {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Autowired
    NumberballMapper numberballMapper;
    ReservationinformationMapper reservationinformationMapper;

    // 添加预约信息 时段格式：09:30
    @Override
    public Map<String, Object> addInfo(HttpServletRequest request, String time_re, Integer center_id, Integer first_id, String window) {
        Map<String, Object> data = new HashMap<>();
        String jwtToken = request.getHeader(tokenHeader);
        User user  = JwtUtils.verifyJwtToken(jwtToken);
        Reservationinformation reservationinformation = new Reservationinformation();

        if(user != null){
            // 根据用户id从redis查询该用户是否已存在惩罚名单中，若存在则不能进行预约
            User punishUser = (User) RedisUtils.getValue("punishUser:" + user.getId());
            if(punishUser == null) {
                // 查询剩余的号球数量
                QueryWrapper<Numberball> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(Numberball::getCenterId, center_id);
                queryWrapper.lambda().eq(Numberball::getFirstId, first_id);
                queryWrapper.lambda().eq(Numberball::getTime, time_re);
                Numberball numberball = numberballMapper.selectOne(queryWrapper);

                if (numberball != null && numberball.getNumber() != 0) {
                    // 减少号球数量
                    LambdaUpdateWrapper<Numberball> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.eq(Numberball::getId, numberball.getId());
                    updateWrapper.set(Numberball::getNumber, numberball.getNumber() - 1);
                    int x = numberballMapper.update(null, updateWrapper);

                    // 添加预约信息
                    reservationinformation.setUserId(user.getId());
                    reservationinformation.setTimeRe(time_re);
                    reservationinformation.setCenterId(center_id);
                    reservationinformation.setFirstId(first_id);
                    reservationinformation.setWindow(window);

                    Date tomorrow = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
                    long currentTime = System.currentTimeMillis();
                    Date date1 = new Date(currentTime);

                    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    System.out.println(formatter2.format(date1));
                    System.out.println(formatter1.format(tomorrow));

                    reservationinformation.setTime(formatter1.format(tomorrow));
                    reservationinformation.setTimeNow(formatter2.format(date1));

                    int i = this.baseMapper.insert(reservationinformation);

                    if (i == 1 && x == 1) {
                        data.put("msg", "预约成功");
                    }
                    return data;
                }
            }
        }
        return null;
    }

    // 根据日期和id查询用户的预约信息
    public Map<String, Object> selectReservationByDataAndId(HttpServletRequest request, String date){
        Map<String, Object> data = new HashMap<>();
        // 从Header中获取token后根据token从redis中获取token对应用户的信息
        String jwtToken = request.getHeader(tokenHeader);
        User user  = JwtUtils.verifyJwtToken(jwtToken);
        if(user != null){
            QueryWrapper<Reservationinformation> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Reservationinformation::getUserId, user.getId());
            queryWrapper.lambda().eq(Reservationinformation::getTime, date);
            List<Reservationinformation> list = this.baseMapper.selectList(queryWrapper);
            if(list != null){
                data.put("reservationInformation", list);
                return data;
            }
        }
        return null;
    }

    // 根据用户id查询当前时间之后的所有预约信息
    public Map<String, Object> selectAllReservation(HttpServletRequest request){
        Map<String, Object> data = new HashMap<>();
        // 从Header中获取token后根据token从redis中获取token对应用户的信息
        String jwtToken = request.getHeader(tokenHeader);
        User user  = JwtUtils.verifyJwtToken(jwtToken);
        if(user != null){
            Date tomorrow = new Date(new Date().getTime() + (1000*60*60*24));
            Date now = new Date();
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            String tomorrowStr = formatter1.format(tomorrow);
            String nowStr = formatter1.format(now);
            QueryWrapper<Reservationinformation> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Reservationinformation::getUserId, user.getId());
            queryWrapper.lambda().eq(Reservationinformation::getTime, tomorrowStr).or().eq(Reservationinformation::getTime, nowStr);
            queryWrapper.lambda().eq(Reservationinformation::getaState, 1);
            List<Reservationinformation> list = this.baseMapper.selectList(queryWrapper);
            if(list != null){
                    data.put("list", list);
                    return data;
            }
        }
        return null;
    }

    // 根据用户id查询当年某个月份的预约信息
    public Map<String, Object> selectReservationByMonAndId(HttpServletRequest request, String Month){
        Map<String, Object> data = new HashMap<>();
        // 从Header中获取token后根据token从redis中获取token对应用户的信息
        String jwtToken = request.getHeader(tokenHeader);
        User user  = JwtUtils.verifyJwtToken(jwtToken);
        if(user != null){
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            String date = zonedDateTime.getYear() + "-" + Month;
            QueryWrapper<Reservationinformation> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Reservationinformation::getUserId, user.getId());
            queryWrapper.lambda().like(Reservationinformation::getTime, date);
            List<Reservationinformation> list = this.baseMapper.selectList(queryWrapper);
            if(list != null){
                data.put("reservationInformation", list);
                return data;
            }
        }
        return null;
    }

    // 根据用户id查询用户当年某个月取消预约的次数
    public Map<String, Object> selectCancelByMonAndId(HttpServletRequest request, String Month){
        Map<String, Object> data = new HashMap<>();
        // 从Header中获取token后根据token从redis中获取token对应用户的信息
        String jwtToken = request.getHeader(tokenHeader);
        User user  = JwtUtils.verifyJwtToken(jwtToken);
        if(user != null){
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            String date = zonedDateTime.getYear() + "-" + Month;
            QueryWrapper<Reservationinformation> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Reservationinformation::getUserId, user.getId());
            queryWrapper.lambda().like(Reservationinformation::getTime, date);
            queryWrapper.lambda().eq(Reservationinformation::getaState, 0);
            List<Reservationinformation> list = this.baseMapper.selectList(queryWrapper);
            if(list != null){
                data.put("listSize", list.size());
                data.put("cancelReservationInformation", list);
                return data;
            }
        }
        return null;
    }

    // 根据用户id查询用户某年某月某天的预约信息
    public Map<String, Object> selectReservation(HttpServletRequest request, String date){
        Map<String, Object> data = new HashMap<>();
        // 从Header中获取token后根据token从redis中获取token对应用户的信息
        String jwtToken = request.getHeader(tokenHeader);
        User user  = JwtUtils.verifyJwtToken(jwtToken);
        if(user != null){
            QueryWrapper<Reservationinformation> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Reservationinformation::getUserId, user.getId());
            queryWrapper.lambda().eq(Reservationinformation::getTime, date);
            List<Reservationinformation> list = this.baseMapper.selectList(queryWrapper);
            if (list != null){
                data.put("reservationInformation", list);
                return data;
            }
        }
        return null;
    }

    // 根据用户id查询用户所有的预约信息
    public Map<String, Object> selectAll(HttpServletRequest request){
        Map<String, Object> data = new HashMap<>();
        String jwtToken = request.getHeader(tokenHeader);
        User user  = JwtUtils.verifyJwtToken(jwtToken);
        if(user != null){
            QueryWrapper<Reservationinformation> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Reservationinformation::getUserId, user.getId());
            List<Reservationinformation> list = this.reservationinformationMapper.selectList(queryWrapper);
            data.put("list", list);
            return data;
        }
        return null;
    }

    // 根据预约信息中的id取消预约，距离预约时段小于2个小时无法取消
    public Map<String, Object> cancelReservation(String id){
        Map<String, Object> data = new HashMap<>();
        Reservationinformation reservationinformation = this.baseMapper.selectById(id);
        if(reservationinformation != null && reservationinformation.getaState() != 0){
            // time为从数据库中查询到的预约日期，格式：2023-06-04
            String time = reservationinformation.getTime();
            // time_re为从数据库中查询到的预约时段，格式：09:00
            String time_re = reservationinformation.getTimeRe();
            Date now = new Date();
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
            // time1为当前的年月日，例如：2023-06-04
            String time1 = formatter1.format(now);
            // time2为当前的时间，例如：21:00
            String time2 = formatter2.format(now);

            String [] timeList = time.split("-");
            String [] timeList1 = time1.split("-");
            String [] time_reList = time_re.split(":");
            String [] time_reList1 = time2.split(":");
            if(Integer.getInteger(timeList1[0]) > Integer.getInteger(timeList[0])){
                return null;
            }
            else if(Integer.getInteger(timeList1[0]) < Integer.getInteger(timeList[0])){
                reservationinformation.setaState(0);
                int i = this.baseMapper.update(reservationinformation, null);
                // 查找所对应的号球
                QueryWrapper<Numberball> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(Numberball::getTime, reservationinformation.getTimeRe());
                queryWrapper.lambda().eq(Numberball::getFirstId, reservationinformation.getFirstId());
                queryWrapper.lambda().eq(Numberball::getCenterId, reservationinformation.getCenterId());
                Numberball numberball = this.numberballMapper.selectOne(queryWrapper);
                // 号球数量+1
                numberball.setNumber(numberball.getNumber() + 1);
                int z = this.numberballMapper.update(numberball, null);

                data.put("success", i);
                return data;
            }
            else {
                if(Integer.getInteger(timeList1[1]) > Integer.getInteger(timeList[1])){
                    return null;
                }
                else if(Integer.getInteger(timeList1[1]) < Integer.getInteger(timeList[1])){
                    reservationinformation.setaState(0);
                    int i = this.baseMapper.update(reservationinformation, null);
                    // 查找所对应的号球
                    QueryWrapper<Numberball> queryWrapper = new QueryWrapper<>();
                    queryWrapper.lambda().eq(Numberball::getTime, reservationinformation.getTimeRe());
                    queryWrapper.lambda().eq(Numberball::getFirstId, reservationinformation.getFirstId());
                    queryWrapper.lambda().eq(Numberball::getCenterId, reservationinformation.getCenterId());
                    Numberball numberball = this.numberballMapper.selectOne(queryWrapper);
                    // 号球数量+1
                    numberball.setNumber(numberball.getNumber() + 1);
                    int z = this.numberballMapper.update(numberball, null);

                    data.put("success", i);
                    return data;
                }
                else {
                    if(Integer.getInteger(timeList1[2]) > Integer.getInteger(timeList[2])){
                        return null;
                    }
                    else if(Integer.getInteger(timeList1[2]) < Integer.getInteger(timeList[2])){
                        reservationinformation.setaState(0);
                        int i = this.baseMapper.update(reservationinformation, null);
                        // 查找所对应的号球
                        QueryWrapper<Numberball> queryWrapper = new QueryWrapper<>();
                        queryWrapper.lambda().eq(Numberball::getTime, reservationinformation.getTimeRe());
                        queryWrapper.lambda().eq(Numberball::getFirstId, reservationinformation.getFirstId());
                        queryWrapper.lambda().eq(Numberball::getCenterId, reservationinformation.getCenterId());
                        Numberball numberball = this.numberballMapper.selectOne(queryWrapper);
                        // 号球数量+1
                        numberball.setNumber(numberball.getNumber() + 1);
                        int z = this.numberballMapper.update(numberball, null);

                        data.put("success", i);
                        return data;
                    }
                    else {
                        if(Integer.getInteger(time_reList1[0]) >= Integer.getInteger(time_reList[0])){
                            return null;
                        }
                        else {
                            if(Integer.getInteger(time_reList[0])-Integer.getInteger(time_reList1[0]) > 2){
                                reservationinformation.setaState(0);
                                int i = this.baseMapper.update(reservationinformation, null);
                                // 查找所对应的号球
                                QueryWrapper<Numberball> queryWrapper = new QueryWrapper<>();
                                queryWrapper.lambda().eq(Numberball::getTime, reservationinformation.getTimeRe());
                                queryWrapper.lambda().eq(Numberball::getFirstId, reservationinformation.getFirstId());
                                queryWrapper.lambda().eq(Numberball::getCenterId, reservationinformation.getCenterId());
                                Numberball numberball = this.numberballMapper.selectOne(queryWrapper);
                                // 号球数量+1
                                numberball.setNumber(numberball.getNumber() + 1);
                                int z = this.numberballMapper.update(numberball, null);

                                data.put("success", i);
                                return data;
                            }
                            else if(Integer.getInteger(time_reList[0])-Integer.getInteger(time_reList1[0]) == 2){
                                if(Integer.getInteger(time_reList1[1]) <= Integer.getInteger(time_reList[1])){
                                    reservationinformation.setaState(0);
                                    int i = this.baseMapper.update(reservationinformation, null);
                                    // 查找所对应的号球
                                    QueryWrapper<Numberball> queryWrapper = new QueryWrapper<>();
                                    queryWrapper.lambda().eq(Numberball::getTime, reservationinformation.getTimeRe());
                                    queryWrapper.lambda().eq(Numberball::getFirstId, reservationinformation.getFirstId());
                                    queryWrapper.lambda().eq(Numberball::getCenterId, reservationinformation.getCenterId());
                                    Numberball numberball = this.numberballMapper.selectOne(queryWrapper);
                                    // 号球数量+1
                                    numberball.setNumber(numberball.getNumber() + 1);
                                    int z = this.numberballMapper.update(numberball, null);

                                    data.put("success", i);
                                    return data;
                                }
                                else {
                                    return null;
                                }
                            }
                            else {
                                return null;
                            }
                        }
                    }
                }
            }

        }
        return null;
    }

    // 判断用户是否需要进行封号处理，若需要则把信息存在redis中，设置自动过期
    public Map<String, Object> isPunish(HttpServletRequest request){
        Map<String, Object> data = new HashMap<>();
        // 从Header中获取token后根据token从redis中获取token对应用户的信息
        String jwtToken = request.getHeader(tokenHeader);
        User user  = JwtUtils.verifyJwtToken(jwtToken);
        if(user != null){
            // 根据用户id从redis查询该用户是否已存在惩罚名单中，若存在则不再添加，不存在则查询是否需要进行惩罚
            User punishUser = (User) RedisUtils.getValue("punishUser:" + user.getId());
            if(punishUser == null){
                // 查询当月取消次数是否达到了3次
                Date now = new Date();
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-");
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
                String time = formatter1.format(now);
                String time1 = formatter2.format(now);
                QueryWrapper<Reservationinformation> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().likeRight(Reservationinformation::getTimeNow, time);
                queryWrapper.lambda().eq(Reservationinformation::getUserId, user.getId());
                queryWrapper.lambda().eq(Reservationinformation::getaState, 0);
                List<Reservationinformation> list = this.reservationinformationMapper.selectList(queryWrapper);
                if(list != null && list.size() >= 3){
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
                    Map<String, String> map = new HashMap<>();
                    map.put("ExpireTime", formatter.format(new Date(System.currentTimeMillis()+1000L * 60 * 60 * 24 * 30)));
                    map.put("LoginTime", formatter.format(new Date(System.currentTimeMillis())));
                    user.setTime(map);
                    RedisUtils.saveValue("punishUser:" + user.getId(), user, 30, TimeUnit.DAYS);
                    data.put("mages", "该用户当月取消预约次数达到了3次");
                    return data;
                }
                else if (list != null){
                    // 查询当天取消次数是否达到了两次
                    QueryWrapper<Reservationinformation> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.lambda().likeRight(Reservationinformation::getTimeNow, time1);
                    queryWrapper1.lambda().eq(Reservationinformation::getUserId, user.getId());
                    queryWrapper1.lambda().eq(Reservationinformation::getaState, 0);
                    List<Reservationinformation> list1 = this.reservationinformationMapper.selectList(queryWrapper1);
                    if(list1 != null && list1.size()>=2){
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
                        Map<String, String> map = new HashMap<>();
                        map.put("ExpireTime", formatter.format(new Date(System.currentTimeMillis()+ 1000L * 60 * 60 * 24)));
                        map.put("LoginTime", formatter.format(new Date(System.currentTimeMillis())));
                        user.setTime(map);
                        RedisUtils.saveValue("punishUser:" + user.getId(), user, 1, TimeUnit.DAYS);
                        data.put("mages", "该用户当天取消预约次数达到了2次");
                        return data;
                    }
                }
                else {
                    // 查询失约次数是否达到了3次
                    QueryWrapper<Reservationinformation> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.lambda().eq(Reservationinformation::getUserId, user.getId());
                    queryWrapper2.lambda().eq(Reservationinformation::getpState, 2);
                    List<Reservationinformation> list1 = this.reservationinformationMapper.selectList(queryWrapper2);
                    if(list1!=null && list1.size()>=3){
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
                        Map<String, String> map = new HashMap<>();
                        map.put("ExpireTime", formatter.format(new Date(System.currentTimeMillis()+ 1000L * 60 * 60 * 24 * 90)));
                        map.put("LoginTime", formatter.format(new Date(System.currentTimeMillis())));
                        user.setTime(map);
                        RedisUtils.saveValue("punishUser:" + user.getId(), user, 90, TimeUnit.DAYS);
                        data.put("mages", "该用户失约次数达到了3次");
                        for(Reservationinformation reservationinformation : list1){
                            reservationinformation.setpState(3);
                            this.reservationinformationMapper.update(reservationinformation, null);
                        }
                        return data;
                    }
                }
            }
        }
        return null;
    }

    // 修改预约信息
    public Map<String, Object> changeInfo(Integer id, String time_re, Integer center_id, Integer first_id, String window){
        Map<String, Object> data = new HashMap<>();
        Reservationinformation reservationinformation = this.reservationinformationMapper.selectById(id);
        if(reservationinformation != null && reservationinformation.getNum() != 1){
            // 增加之前预约时段的号球数
            UpdateWrapper<Numberball> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(Numberball::getCenterId, reservationinformation.getCenterId());
            updateWrapper.lambda().eq(Numberball::getFirstId, reservationinformation.getFirstId());
            updateWrapper.lambda().eq(Numberball::getTime, reservationinformation.getTimeRe());
            updateWrapper.setSql("number = number+1");
            int i = this.numberballMapper.update(null, updateWrapper);

            // 修改预约信息
            reservationinformation.setTimeRe(time_re);
            reservationinformation.setCenterId(center_id);
            reservationinformation.setFirstId(first_id);
            reservationinformation.setWindow(window);
            reservationinformation.setNum(1);
            int x = this.reservationinformationMapper.updateById(reservationinformation);

            // 减少修改后的预约时段的号球数
            UpdateWrapper<Numberball> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.lambda().eq(Numberball::getCenterId, center_id);
            updateWrapper1.lambda().eq(Numberball::getFirstId, first_id);
            updateWrapper1.lambda().eq(Numberball::getTime, time_re);
            updateWrapper1.setSql("number = number-1");
            int z = this.numberballMapper.update(null, updateWrapper1);
            
            data.put("add", i);
            data.put("change", x);
            data.put("sub", z);
            return data;

        }
        return null;
    }
}
