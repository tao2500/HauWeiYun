package com.bdc.adminService.controller;

import com.bdc.adminService.common.vo.Result;
import com.bdc.adminService.common.vo.ResultCode;
import com.bdc.adminService.service.IFirstbusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/adminService/firstbusiness")
public class FirstbusinessController {

    @Autowired
    IFirstbusinessService firstbusinessService;

    // 查询所有一级业务
    @GetMapping("/selectAllFirst")
    public Result selectAllFirst(){
        Map<String, Object> data = firstbusinessService.selectAllFirst();
        if(data != null){
            return Result.success(data);
        }
        return Result.failure(ResultCode.DATA_NONE);
    }

    // 添加新的一级业务
    @PostMapping("/addFirst")
    public Result addFirst(@RequestParam String name){
        Map<String, Object> data = firstbusinessService.addFirst(name);
        if(data != null){
            return Result.success(data);
        }
        return Result.failure(ResultCode.ERROR);
    }

}
