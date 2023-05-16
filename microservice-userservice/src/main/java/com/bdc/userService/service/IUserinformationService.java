package com.bdc.userService.service;

import com.bdc.userService.entity.Userinformation;
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
public interface IUserinformationService extends IService<Userinformation> {

    Map<String, Object> selectUserInfo(HttpServletRequest request);

    Map<String, Object> addInfo(HttpServletRequest request, String name, String idNo);
}
