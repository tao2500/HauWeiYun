package com.bdc.reservationService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdc.reservationService.entity.Numberball;
import com.bdc.reservationService.entity.Reservationinformation;
import com.bdc.reservationService.mapper.NumberballMapper;
import com.bdc.reservationService.mapper.ReservationinformationMapper;
import com.bdc.reservationService.service.IReservationinformationService;
import com.bdc.reservationService.util.JwtUtils;
import com.bdc.userService.entity.User;
import com.bdc.adminService.entity.*;
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

    // 逻辑未完成   时段格式：09:30
    @Override
    public Map<String, Object> addInfo(HttpServletRequest request, String time_re, Integer center_id, Integer first_id) {
        Map<String, Object> data = new HashMap<>();
        String jwtToken = request.getHeader(tokenHeader);
//        System.out.println("jwtToken:" + jwtToken);
        User user  = JwtUtils.verifyJwtToken(jwtToken);
//        System.out.print("解析token:" + user);
        Reservationinformation reservationinformation = new Reservationinformation();

        if(user != null){
            Timeinterval timeInterval = reservationinformationMapper.selectTimeId(time_re);
            Integer time_id = timeInterval.getId();
            QueryWrapper<Numberball> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Numberball::getCenterId, center_id);
            queryWrapper.lambda().eq(Numberball::getFirstId, first_id);
            queryWrapper.lambda().eq(Numberball::getTimeId, time_id);
            Numberball numberball = numberballMapper.selectOne(queryWrapper);

            if(numberball != null && numberball.getNumber() != 0){

                LambdaUpdateWrapper<Numberball> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(Numberball::getId, numberball.getId());
                updateWrapper.set(Numberball::getNumber, numberball.getNumber()-1);
                int x = numberballMapper.update(null, updateWrapper);

                reservationinformation.setUserId(user.getId());
                reservationinformation.setTimeRe(time_re);
                reservationinformation.setCenterId(center_id);
                reservationinformation.setFirstId(first_id);

//                LocalDate date = LocalDate.now();
                Date tomorrow = new Date(new Date().getTime() + (1000*60*60*24));
                long currentTime = System.currentTimeMillis();
                Date date1 = new Date(currentTime);

                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                System.out.println(formatter2.format(date1));
                System.out.println(formatter1.format(tomorrow));

                reservationinformation.setTime(formatter1.format(tomorrow));
                reservationinformation.setTimeNow(formatter2.format(date1));

                int i = this.baseMapper.insert(reservationinformation);

                if(i == 1 && x == 1){
                    data.put("msg", "预约成功");
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

    // 根据预约信息中的id取消预约，距离预约时段小于2个小时无法取消
    public Map<String, Object> cancelReservation(String id){
        Map<String, Object> data = new HashMap<>();
        Reservationinformation reservationinformation = this.baseMapper.selectById(id);
        if(reservationinformation != null){
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
                                data.put("success", i);
                                return data;
                            }
                            else if(Integer.getInteger(time_reList[0])-Integer.getInteger(time_reList1[0]) == 2){
                                if(Integer.getInteger(time_reList1[1]) <= Integer.getInteger(time_reList[1])){
                                    reservationinformation.setaState(0);
                                    int i = this.baseMapper.update(reservationinformation, null);
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
}
