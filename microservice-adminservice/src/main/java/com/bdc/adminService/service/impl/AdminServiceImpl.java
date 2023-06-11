package com.bdc.adminService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdc.adminService.entity.Admin;
import com.bdc.adminService.mapper.AdminMapper;
import com.bdc.adminService.service.IAdminService;
import com.bdc.adminService.util.JwtUtils;
import com.bdc.adminService.util.RedisUtils;
import com.bdc.userService.entity.User;
import com.bdc.userService.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bc
 * @since 2023-05-15
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private RestTemplate restTemplate;

    @Resource
    public UserMapper userMapper;

    @Override
    public Map<String, Object> login(String userName, String passWord) {
        String token;
        Admin admin;
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<Admin>();
        queryWrapper.lambda().eq(Admin::getPhone, userName);
        queryWrapper.lambda().eq(Admin::getPaswd, passWord);
        admin = this.baseMapper.selectOne(queryWrapper);
        if (admin != null && admin.getPower()==1){ //一个简单的登录逻辑
            Admin jwtAdmin = JwtUtils.setTime(admin);
            RedisUtils.saveValue("admin:" + jwtAdmin.getId(), jwtAdmin,30, TimeUnit.MINUTES); //将用户信息存入redis数据库 第三和第四个参数为有效时间和时间单位
            Map<String,Object> userInfoMap = new HashMap<String, Object>();
            userInfoMap.put("id", jwtAdmin.getId());
            token = JwtUtils.createJwtToken(userInfoMap); //使用工具类生成token
            userInfoMap.put("token", token);
            System.out.println("当前管理员的token:" + token);
            return userInfoMap;
        }else {
            return null;
        }
    }

    // 查询所有用户
    @Override
    public String selectAllUser() {
        return this.restTemplate.getForObject("http://user-service/user/selectAllUser", String.class);
    }

    @Override
    public Map<String, Object> selectByPhone(String phone) {
        Map<String, Object> data = new HashMap<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getPhone, phone);
        User user = this.userMapper.selectOne(queryWrapper);
        data.put("user", user);
        return data;
    }


}
