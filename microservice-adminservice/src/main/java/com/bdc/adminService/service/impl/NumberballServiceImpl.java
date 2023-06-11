package com.bdc.adminService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bdc.adminService.entity.Numberball;
import com.bdc.adminService.mapper.NumberballMapper;
import com.bdc.adminService.service.INumberballService;
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
public class NumberballServiceImpl extends ServiceImpl<NumberballMapper, Numberball> implements INumberballService {

    // 查询所有号球数量
    @Override
    public Map<String, Object> selectAllFirst() {
        Map<String, Object> data = new HashMap<>();
        List<Numberball> list = this.baseMapper.selectList(null);
        data.put("list", list);
        return data;
    }
}
