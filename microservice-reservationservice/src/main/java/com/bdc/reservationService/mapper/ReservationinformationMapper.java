package com.bdc.reservationService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bdc.adminService.entity.Timeinterval;
import com.bdc.reservationService.entity.Reservationinformation;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bc
 * @since 2023-05-16
 */
@Mapper
public interface ReservationinformationMapper extends BaseMapper<Reservationinformation> {
    public Timeinterval selectTimeId(String time_re);

}
