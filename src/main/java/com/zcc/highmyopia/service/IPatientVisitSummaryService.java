package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcc.highmyopia.po.PatientVisitSummaryView;


public interface IPatientVisitSummaryService extends IService<PatientVisitSummaryView> {
    IPage<PatientVisitSummaryView> getPatientVisitSummaryByPage(
        String visitNumber, Long patientId, Page<PatientVisitSummaryView> page);
}
