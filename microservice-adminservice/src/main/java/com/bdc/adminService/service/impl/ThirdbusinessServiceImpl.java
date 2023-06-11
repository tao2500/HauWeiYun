package com.bdc.adminService.service.impl;

import com.bdc.adminService.entity.Thirdbusiness;
import com.bdc.adminService.mapper.ThirdbusinessMapper;
import com.bdc.adminService.service.IThirdbusinessService;
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
public class ThirdbusinessServiceImpl extends ServiceImpl<ThirdbusinessMapper, Thirdbusiness> implements IThirdbusinessService {

    // 查询所有三级业务
    @Override
    public Map<String, Object> selectAll() {
        Map<String, Object> data = new HashMap<>();
        List<Thirdbusiness> list = this.baseMapper.selectList(null);
        data.put("list", list);
        return data;
    }
}
