package com.bdc.adminService.util;

import com.bdc.adminService.entity.Admin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    /**
     * 自定义秘钥
     * */
    private static String sign;

    /**
     * jwtToken的默认有效时间 单位分钟
     * */
    private static int expireTime;

    @Value("${jwt.sign}")
    public void setSign(String sign1){
        JwtUtils.sign = sign1;
    }

    @Value("${jwt.expireTime}")
    public void setExpireTime(int expireTime1){
        JwtUtils.expireTime = expireTime1;
    }

    /**
     * 生成jwt token
     * @param map  要存放负载信息
     * */
    public static String createJwtToken(Map<String,Object> map){
        return  Jwts.builder()
                .setClaims(map) //放入payLoad部分的信息
                .signWith(SignatureAlgorithm.HS512,sign)
                .compact();

    }


    /**
     * 从令牌中获取数据,就是payLoad部分存放的数据。如果jwt被改，该函数会直接抛出异常
     * @param token  令牌
     * */
    public static Claims  parseToken(String token){
        System.out.println(token);
        return Jwts.parser()
                .setSigningKey(sign)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证用户信息
     * @param token  jwtToken
     * */
    public static Admin verifyJwtToken(String token){
        Claims claims = parseToken(token);
        System.out.println("claims:::"+claims.toString());
        String id = String.valueOf(claims.get("id"));
        System.out.println("id:::"+id);
        //从redis中获取用户信息
        Object user = RedisUtils.getValue("admin:" + id);
        Admin admin1 = (Admin) user;

        return admin1;
    }


    /**
     * 刷新令牌时间，刷新redis缓存时间
     * @param  admin 用户信息
     * */
    public static void refreshToken(Admin admin){
        //重新设置User对象的过期时间，再刷新缓存
        admin.setExpireTime(System.currentTimeMillis()+1000L * 60 * expireTime);
        RedisUtils.saveValue("admin:" + admin.getId()+"", admin,expireTime,TimeUnit.MINUTES);
    }

    /**
     * 设置用户的登录时间和令牌有效时间
     * @param admin
     */
    public static Admin setTime(Admin admin){
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年-MM月dd日-HH时mm分ss秒");
        Date date = new Date(currentTime);
        System.out.println("登录时间：" + formatter.format(date));

        admin.setExpireTime(System.currentTimeMillis()+1000L * 60 * expireTime);
        admin.setLoginTime(System.currentTimeMillis());

        Map<String, String> map = new HashMap<>();
        map.put("ExpireTime", formatter.format(new Date(System.currentTimeMillis()+1000L * 60 * expireTime)));
        map.put("LoginTime", formatter.format(new Date(System.currentTimeMillis())));
        admin.setTime(map);

        return admin;
    }
}

