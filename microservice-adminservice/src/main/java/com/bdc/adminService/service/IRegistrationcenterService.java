package com.bdc.adminService.service;

import com.bdc.adminService.entity.Registrationcenter;
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
public interface IRegistrationcenterService extends IService<Registrationcenter> {

    Map<String, Object> selectAll();
}
