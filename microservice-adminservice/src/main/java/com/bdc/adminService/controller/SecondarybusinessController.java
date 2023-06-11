package com.bdc.adminService.controller;

import com.bdc.adminService.common.vo.Result;
import com.bdc.adminService.common.vo.ResultCode;
import com.bdc.adminService.service.ISecondarybusinessService;
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
@RequestMapping("/adminService/secondarybusiness")
public class SecondarybusinessController {

    @Autowired
    ISecondarybusinessService secondarybusinessService;

    // 查询所有二级业务
    @GetMapping("/selectAll")
    public Result selectAll(){
        Map<String, Object> data = secondarybusinessService.selectAll();
        if(data != null){
            return Result.success(data);
        }
        return Result.failure(ResultCode.DATA_NONE);
    }

}
