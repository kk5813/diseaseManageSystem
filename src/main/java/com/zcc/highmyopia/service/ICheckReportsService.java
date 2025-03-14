package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.CheckResults;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
public interface ICheckReportsService extends IService<CheckReports> {
    List<CheckReports> getCheckReportById(Long patientId, String visitNumber);

    List<CheckReports> getCheckReportByVisitNumber(String visitNumber);
}
