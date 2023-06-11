package com.bdc.adminService.service;

import com.bdc.adminService.entity.Secondarybusiness;
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
public interface ISecondarybusinessService extends IService<Secondarybusiness> {

    Map<String, Object> selectAll();
}
