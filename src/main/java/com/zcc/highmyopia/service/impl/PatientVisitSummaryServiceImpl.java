package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.mapper.IPatientVisitSummaryViewMapper;
import com.zcc.highmyopia.mapper.IPatientsMapper;
import com.zcc.highmyopia.po.PatientVisitSummaryView;
import com.zcc.highmyopia.po.Patients;
import com.zcc.highmyopia.service.IPatientVisitSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientVisitSummaryServiceImpl extends ServiceImpl<IPatientVisitSummaryViewMapper, PatientVisitSummaryView> implements IPatientVisitSummaryService {

    @Autowired
    private IPatientVisitSummaryViewMapper patientVisitSummaryMapper;

    @Override
    public IPage<PatientVisitSummaryView> getPatientVisitSummaryByPage(
            String visitNumber, Long patientId, Page<PatientVisitSummaryView> page) {
        // 进行去重
        List<PatientVisitSummaryView> result = patientVisitSummaryMapper
                .selectByVisitNumberOrPatientId(visitNumber, patientId).stream().distinct().collect(Collectors.toList());
        page.setRecords(result);
        page.setTotal(result.size());
        return page;
    }
}
