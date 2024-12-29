package com.zcc.highmyopia.AI.repository;

import com.zcc.highmyopia.AI.model.valobj.RuleTreeVO;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.ReportFiles;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
public interface IDiagnoseRepository {

    List<CheckReports> getCheckReport(Long patientId);

    List<ReportFiles> getReportFile(Long id);

    RuleTreeVO getRuleTreeVOByDiseaseId(Integer diseaseId);
}
