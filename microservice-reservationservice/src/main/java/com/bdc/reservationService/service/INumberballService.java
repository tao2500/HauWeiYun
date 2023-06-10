package com.bdc.reservationService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bdc.reservationService.entity.Numberball;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bc
 * @since 2023-05-16
 */
public interface INumberballService extends IService<Numberball> {

    /**
     * 根据不动产中心的id查询该不动产中心还剩余的号球数量
     */
    Map<String, Object> selectAll(Integer id);

}
