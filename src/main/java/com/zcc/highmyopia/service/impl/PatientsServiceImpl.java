package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zcc.highmyopia.po.Patients;
import com.zcc.highmyopia.mapper.IPatientsMapper;
import com.zcc.highmyopia.service.IPatientsService;
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
public class PatientsServiceImpl extends ServiceImpl<IPatientsMapper, Patients> implements IPatientsService {

    @Autowired
    IPatientsMapper patientMapper;

    @Override
    public List<Patients> pageQuery(Integer pageNumber, Integer pageSize) {
        Page<Patients> page = new Page<>(pageNumber, pageSize);
        IPage<Patients> pages = this.page(page);
        return pages.getRecords();
    }
}
