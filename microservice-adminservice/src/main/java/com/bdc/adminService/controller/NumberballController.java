package com.bdc.adminService.controller;

import com.bdc.adminService.common.vo.Result;
import com.bdc.adminService.common.vo.ResultCode;
import com.bdc.adminService.service.INumberballService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author bc
 * @since 2023-05-15
 */
@RestController
@RequestMapping("/adminService/numberball")
public class NumberballController {

    @Autowired
    INumberballService numberballService;

    @GetMapping("/selectAll")
    public Result selectAll(){
        Map<String, Object> data = numberballService.selectAllFirst();
        if(data != null){
            return Result.success(data);
        }
        return Result.failure(ResultCode.DATA_NONE);
    }

}
