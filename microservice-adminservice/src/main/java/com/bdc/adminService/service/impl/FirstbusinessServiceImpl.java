package com.bdc.adminService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdc.adminService.entity.Firstbusiness;
import com.bdc.adminService.mapper.FirstbusinessMapper;
import com.bdc.adminService.service.IFirstbusinessService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bc
 * @since 2023-05-15
 */
@Service
public class FirstbusinessServiceImpl extends ServiceImpl<FirstbusinessMapper, Firstbusiness> implements IFirstbusinessService {

    // 查询所有一级业务
    @Override
    public Map<String, Object> selectAllFirst() {
        Map<String, Object> data = new HashMap<>();
        List<Firstbusiness> list;
        QueryWrapper<Firstbusiness> queryWrapper = new QueryWrapper<Firstbusiness>();
        list = this.baseMapper.selectList(queryWrapper);
        if(list != null){
            data.put("list", list);
            return data;
        }
        return null;
    }

    // 添加新的一级业务
    @Override
    public Map<String, Object> addFirst(String name) {
        Map<String, Object> data = new HashMap<>();
        Firstbusiness firstbusiness = new Firstbusiness();
        firstbusiness.setName(name);
        int i = this.baseMapper.insert(firstbusiness);
        if(i != 0){
            data.put("i", i);
            return data;
        }
        return null;
    }



}
