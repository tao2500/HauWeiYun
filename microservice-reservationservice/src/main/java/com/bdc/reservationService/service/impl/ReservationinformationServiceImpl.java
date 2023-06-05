package com.bdc.reservationService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdc.reservationService.entity.Reservationinformation;
import com.bdc.reservationService.entity.User;
import com.bdc.reservationService.mapper.ReservationinformationMapper;
import com.bdc.reservationService.service.IReservationinformationService;
import com.bdc.reservationService.util.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bc
 * @since 2023-05-16
 */
@Service
public class ReservationinformationServiceImpl extends ServiceImpl<ReservationinformationMapper, Reservationinformation> implements IReservationinformationService {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;

    // 逻辑未完成
    @Override
    public Map<String, Object> addInfo(HttpServletRequest request, Integer time_id, Integer center_id, Integer first_id) {
        Map<String, Object> data = new HashMap<>();
        String jwtToken = request.getHeader(tokenHeader);
        User user  = JwtUtils.verifyJwtToken(jwtToken);
        Reservationinformation reservationinformation = new Reservationinformation();
        if(user != null){
            reservationinformation.setUserId(user.getId());
            reservationinformation.setTimeId(time_id);
            reservationinformation.setCenterId(center_id);
            reservationinformation.setFirstId(first_id);

            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            reservationinformation.setTime(date.format(formatter));

            int i = this.baseMapper.insert(reservationinformation);

            if(i == 1){
                data.put("msg", "预约成功");
                return data;
            }
        }
        return null;
    }
}
