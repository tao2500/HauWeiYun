package com.bdc.adminService.controller;

import com.alibaba.fastjson.JSONObject;
import com.bdc.adminService.common.vo.Result;
import com.bdc.adminService.common.vo.ResultCode;
import com.bdc.adminService.service.IAdminService;
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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    IAdminService adminService;

    // 管理员登录
    @PostMapping("/login")
    public Result login(@RequestParam(value = "userName") String userName, @RequestParam(value = "passWord")String passWord){
        Map<String, Object> data = adminService.login(userName, passWord);
        if(data != null){
            return Result.success(data);
        }
        return Result.failure(ResultCode.ERROR);
    }

    // 查询所有用户信息
    @GetMapping("/selectAllUser")
    public JSONObject selectAllUser(){
        String str = adminService.selectAllUser();
        System.out.println(str);

        JSONObject jsonObject = JSONObject.parseObject(str);
        if(jsonObject != null){
            return jsonObject;
        }
        return JSONObject.parseObject("{\"code\":2,\"msg\":\"操作失败\"");
    }




}
