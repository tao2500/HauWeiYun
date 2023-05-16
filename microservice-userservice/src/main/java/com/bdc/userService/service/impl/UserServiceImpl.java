package com.bdc.userService.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdc.userService.entity.User;
import com.bdc.userService.entity.User1;
import com.bdc.userService.mapper.UserMapper;
import com.bdc.userService.service.IUserService;
import com.bdc.userService.util.JwtUtils;
import com.bdc.userService.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
 * @since 2023-05-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    @Override
    public Map<String, Object> register(String userName, String passWord) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,userName);
//        queryWrapper.eq(User::getPaswd,passWord);
        User registerUser = this.baseMapper.selectOne(queryWrapper);
        if(registerUser == null){
            HashMap<String,Object> hashMap = new HashMap<>();
            registerUser = new User();
            registerUser.setPhone(userName);
            registerUser.setPaswd(passWord);
            int i = this.baseMapper.insert(registerUser);
            if(i == 1){
                hashMap.put("response", "注册成功");
                return  hashMap;
            }
            else{
                hashMap.put("response", "系统异常，请稍后再试");
            }
        }
        return null;
    }

    // 登录
    @Override
    public Map<String, Object> login(String userName, String passWord) {
        String token;
        User user;
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.lambda().eq(User::getPhone, userName);
        queryWrapper.lambda().eq(User::getPaswd, passWord);
        user = this.baseMapper.selectOne(queryWrapper);
        if (user != null){ //一个简单的登录逻辑
            User jwtUser = JwtUtils.setTime(user);
            RedisUtils.saveValue("user:" + jwtUser.getId()+"",jwtUser,30, TimeUnit.MINUTES); //将用户信息存入redis数据库 第三和第四个参数为有效时间和时间单位
            Map<String,Object> userInfoMap = new HashMap<String, Object>();
            userInfoMap.put("id",jwtUser.getId());
            token = JwtUtils.createJwtToken(userInfoMap); //使用工具类生成token
            userInfoMap.put("token", token);
            System.out.println("当前用户的token:" + token);
            return userInfoMap;
        }else {
            return null;
        }
    }

    @Override
    public Map<String, Object> changePassword(HttpServletRequest request, String oldPassword, String newPassword) {
        Map<String, Object> failMagges = new HashMap<>();
        String jwtToken = request.getHeader(tokenHeader);
        User user  = JwtUtils.verifyJwtToken(jwtToken);
        if(user != null && !oldPassword.equals("") && !newPassword.equals("")){
            User loginUser = JSON.parseObject(JSON.toJSONString(user), User.class);
            Map<String, Object> data = new HashMap<>();
            User loginUser1 = this.baseMapper.selectById(loginUser.getId());
            String password = loginUser1.getPaswd();
            if(oldPassword.equals(password)){
                if(password.equals(newPassword)){
                    failMagges.put("fail1", "新密码与旧密码一致");
                    return failMagges;
                }
                LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(User::getId, loginUser.getId());
                updateWrapper.set(User::getPaswd, newPassword);
                int i = this.baseMapper.update(null, updateWrapper);
                data.put("change", i);
                return data;
            }
            else {
                return null;
            }
        }
        else {
            failMagges.put("fail", "操作失败，未知指定错误信息");
            return failMagges;
        }
    }

    @Override
    public Map<String, Object> selectAllUser() {
        Map<String, Object> data = new HashMap<>();
        List<User> list = this.baseMapper.selectList(null);
        List<User1> list1 = new ArrayList<>();
        if(list != null){
            for (User user:
                    list) {
                User1 user1 = new User1();
                user1.setId(user.getId());
                user1.setPhone(user.getPhone());
                user1.setPaswd(user.getPaswd());
                user1.setState(user.getState());
                user1.setPower(user.getPower());
                list1.add(user1);
            }
            data.put("userList", list1);
            return data;
        }
        return null;
    }
}
