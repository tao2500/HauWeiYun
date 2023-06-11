package com.bdc.adminService.service.impl;

import com.bdc.adminService.entity.Registrationcenter;
import com.bdc.adminService.mapper.RegistrationcenterMapper;
import com.bdc.adminService.service.IRegistrationcenterService;
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
public class RegistrationcenterServiceImpl extends ServiceImpl<RegistrationcenterMapper, Registrationcenter> implements IRegistrationcenterService {

    // 查询所有不动产登记中心
    @Override
    public Map<String, Object> selectAll() {
        Map<String, Object> data = new HashMap<>();
        List<Registrationcenter> list = this.baseMapper.selectList(null);
        data.put("list", list);
        return data;
    }
}
