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

    Map<String, Object> addInfo(HttpServletRequest request, String time_re, Integer center_id, Integer first_id, String window);

    // 根据用户id查询当前时间之后的所有预约信息
    Map<String, Object> selectAllReservation(HttpServletRequest request);

    // 根据用户id查询当年某个月份的预约信息
    Map<String, Object> selectReservationByMonAndId(HttpServletRequest request, String Month);

    // 根据用户id查询用户当年某个月取消预约的次数
    Map<String, Object> selectCancelByMonAndId(HttpServletRequest request, String Month);

    // 根据用户id查询用户某年某月某天的预约信息
    Map<String, Object> selectReservation(HttpServletRequest request, String date);

    // 根据用户id查询用户所有的预约信息
    Map<String, Object> selectAll(HttpServletRequest request);

    // 根据预约信息中的id取消预约，距离预约时段小于2个小时无法取消
    Map<String, Object> cancelReservation(String id);

    // 判断用户是否需要进行封号处理，若需要则把信息存在redis中，设置自动过期
    Map<String, Object> isPunish(HttpServletRequest request);

    // 修改预约信息
    Map<String, Object> changeInfo(Integer id, String time_re, Integer center_id, Integer first_id, String window);

}
