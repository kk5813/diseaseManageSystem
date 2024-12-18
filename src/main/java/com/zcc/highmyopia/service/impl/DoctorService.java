package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.mapper.IDoctorMapper;
import com.zcc.highmyopia.po.Doctor;
import com.zcc.highmyopia.service.IDoctorService;
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

}
