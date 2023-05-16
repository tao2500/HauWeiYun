package com.bdc.userService.service;

import com.bdc.userService.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bc
 * @since 2023-05-13
 */
public interface IUserService extends IService<User> {

    Map<String, Object> register(String userName, String passWord);

    Map<String, Object> login(String userName, String passWord);

    Map<String, Object> changePassword(HttpServletRequest request, String oldPassword, String newPassword);

    Map<String, Object> selectAllUser();
}
