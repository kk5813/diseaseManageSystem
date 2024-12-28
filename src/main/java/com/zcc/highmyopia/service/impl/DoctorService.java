package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.mapper.IDeptMapper;
import com.zcc.highmyopia.mapper.IDoctorMapper;
import com.zcc.highmyopia.po.Dept;
import com.zcc.highmyopia.po.Doctor;
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
public class DoctorService extends ServiceImpl<IDoctorMapper, Doctor> implements IDoctorService{

    private final IDoctorMapper doctorMapper;
    private final IRedisService redisService;

    @Override
    public Doctor getDoctorById(Long doctorId) {
        String cacheKey = Constants.RedisKey.DOCTOR + doctorId;
        Doctor doctor = redisService.getValue(cacheKey);
        if (doctor != null) return doctor;

        doctor = doctorMapper.selectById(doctorId);
        redisService.setValue(cacheKey, doctor);
        return doctor;
    }
}
