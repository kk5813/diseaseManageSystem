package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.highmyopia.entity.Patients;
import com.zcc.highmyopia.mapper.PatientMapper;
import com.zcc.highmyopia.service.PatientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liangyue
 * @since 2021-02-05
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patients> implements PatientService {

    @Autowired
    PatientMapper patientMapper;

    @Override
    public List<Patients> pageQuery(Integer pageNumber, Integer pageSize) {
        Page<Patients> page = new Page<>(pageNumber, pageSize);
        IPage<Patients> pages = this.page(page);
        return pages.getRecords();
    }
}
