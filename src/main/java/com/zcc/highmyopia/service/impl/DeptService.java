package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.mapper.IDeptMapper;
import com.zcc.highmyopia.mapper.IDoctorMapper;
import com.zcc.highmyopia.po.Dept;
import com.zcc.highmyopia.po.Doctor;
import com.zcc.highmyopia.po.Patients;
import com.zcc.highmyopia.po.Site;
import com.zcc.highmyopia.service.IDeptService;
import com.zcc.highmyopia.service.IDoctorService;
import com.zcc.highmyopia.service.IRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Service
@RequiredArgsConstructor
public class DeptService extends ServiceImpl<IDeptMapper, Dept> implements IDeptService{

    private final IDeptMapper deptMapper;
    private final IRedisService redisService;

    @Override
    public Dept getDeptById(Long deptId) {
        String cacheKey = Constants.RedisKey.DEPT + deptId;
        Dept dept = redisService.getValue(cacheKey);
        if (dept != null) return dept;

        dept = deptMapper.selectById(deptId);
        redisService.setValue(cacheKey, dept);
        return dept;
    }
}
