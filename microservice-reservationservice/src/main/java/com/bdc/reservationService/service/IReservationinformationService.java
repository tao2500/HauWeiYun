package com.bdc.reservationService.service;

import com.bdc.reservationService.entity.Reservationinformation;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bc
 * @since 2023-05-16
 */
public interface IReservationinformationService extends IService<Reservationinformation> {

    Map<String, Object> addInfo(HttpServletRequest request, String time_re, Integer center_id, Integer first_id);
}
