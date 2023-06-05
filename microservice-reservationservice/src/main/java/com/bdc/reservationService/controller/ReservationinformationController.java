package com.bdc.reservationService.controller;

import com.bdc.reservationService.common.vo.Result;
import com.bdc.reservationService.common.vo.ResultCode;
import com.bdc.reservationService.service.IReservationinformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author bc
 * @since 2023-05-16
 */
@RestController
@RequestMapping("/reservationService/reservationinformation")
public class ReservationinformationController {

    @Autowired
    IReservationinformationService reservationinformationService;

    // 添加预约信息
    @PostMapping("/addInfo")
    public Result addInfo(HttpServletRequest request,
                          @RequestParam String time_re,
                          @RequestParam Integer center_id,
                          @RequestParam Integer first_id){
        Map<String, Object> data = reservationinformationService.addInfo(request, time_re, center_id, first_id);
        if(data != null){
            return Result.success(data);
        }
        return Result.failure(ResultCode.ERROR);
    }

}
