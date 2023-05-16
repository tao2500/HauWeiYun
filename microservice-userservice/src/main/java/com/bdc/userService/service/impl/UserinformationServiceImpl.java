package com.bdc.userService.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bdc.userService.entity.User;
import com.bdc.userService.entity.Userinformation;
import com.bdc.userService.mapper.UserinformationMapper;
import com.bdc.userService.service.IUserinformationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdc.userService.util.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bc
 * @since 2023-05-13
 */
@Service
public class UserinformationServiceImpl extends ServiceImpl<UserinformationMapper, Userinformation> implements IUserinformationService {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    // 查询用户实名信息
    @Override
    public Map<String, Object> selectUserInfo(HttpServletRequest request) {
        Map<String, Object> failMagges = new HashMap<>();
        String jwtToken = request.getHeader(tokenHeader);
        User user  = JwtUtils.verifyJwtToken(jwtToken);
        if(user != null){
            User loginUser = JSON.parseObject(JSON.toJSONString(user), User.class);
            Map<String, Object> data = new HashMap<>();
            LambdaQueryWrapper<Userinformation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Userinformation::getUserId, loginUser.getId());
            Userinformation userinformation = this.baseMapper.selectOne(queryWrapper);
            if(userinformation != null){
                data.put("info", userinformation);
                return data;
            }
            else {
                data.put("fail", "该用户还未进行实名认证");
                return data;
            }
        }
        return null;
    }

    // 用户添加实名信息
    @Override
    public Map<String, Object> addInfo(HttpServletRequest request, String name, String idNo) {
        Map<String, Object> failMagges = new HashMap<>();
        String jwtToken = request.getHeader(tokenHeader);
        User user  = JwtUtils.verifyJwtToken(jwtToken);
        if (user != null){
            User loginUser = JSON.parseObject(JSON.toJSONString(user), User.class);
            Map<String, Object> data = new HashMap<>();
            Userinformation userinformation = new Userinformation();
            userinformation.setUserId(loginUser.getId());
            userinformation.setName(name);
            userinformation.setIdNo(idNo);
            try {
                int i = this.baseMapper.insert(userinformation);
                if(i == 1){
                    data.put("success", "认证成功");
                }
                else {
                    data.put("fail", "认证失败");
                }
                return data;
            }
            catch (Exception e){
                e.printStackTrace();
                data.put("fail", "实名信息已存在，请勿重复实名");
                return data;
            }
        }
        return null;
    }
}
