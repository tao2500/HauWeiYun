package com.bdc.adminService.service.impl;

import com.bdc.adminService.entity.Secondarybusiness;
import com.bdc.adminService.mapper.SecondarybusinessMapper;
import com.bdc.adminService.service.ISecondarybusinessService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SecondarybusinessServiceImpl extends ServiceImpl<SecondarybusinessMapper, Secondarybusiness> implements ISecondarybusinessService {

    @Override
    public Map<String, Object> selectAll() {
        Map<String, Object> data = new HashMap<>();
        List<Secondarybusiness> list = this.baseMapper.selectList(null);
        data.put("list", list);
        return data;
    }
}
