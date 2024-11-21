package com.zcc.highmyopia.service.impl;

import com.zcc.highmyopia.entity.Patient;
import com.zcc.highmyopia.mapper.PatientMapper;
import com.zcc.highmyopia.service.PatientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liangyue
 * @since 2021-02-05
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

}
