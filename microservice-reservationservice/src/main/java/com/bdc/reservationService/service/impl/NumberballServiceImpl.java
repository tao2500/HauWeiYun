package com.bdc.reservationService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bdc.reservationService.entity.Numberball;
import com.bdc.reservationService.mapper.NumberballMapper;
import com.bdc.reservationService.service.INumberballService;
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
 * @since 2023-05-16
 */
@Service
public class NumberballServiceImpl extends ServiceImpl<NumberballMapper, Numberball> implements INumberballService {

    /**
     * 根据不动产中心的id查询该不动产中心还剩余的号球数量
     */
    @Override
    public Map<String, Object> selectAll(Integer id) {
        Map<String, Object> data = new HashMap<>();
        QueryWrapper<Numberball> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Numberball::getCenterId, id);
        List<Numberball> list = this.baseMapper.selectList(queryWrapper);
        data.put("list", list);
        return data;
    }
}
