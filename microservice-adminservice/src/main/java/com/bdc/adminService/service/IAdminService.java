package com.bdc.adminService.service;

import com.bdc.adminService.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bc
 * @since 2023-05-15
 */
public interface IAdminService extends IService<Admin> {

    Map<String, Object> login(String userName, String passWord);

    String selectAllUser();
}
