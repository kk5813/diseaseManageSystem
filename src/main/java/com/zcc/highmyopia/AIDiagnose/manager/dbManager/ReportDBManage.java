package com.zcc.highmyopia.AIDiagnose.manager.dbManager;

import com.zcc.highmyopia.hospital.entity.CheckReportsEntity;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.ReportFiles;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ReportDBManage
 * @Description
 * @Author aigao
 * @Date 2026/1/17 23:58
 * @Version 1.0
 */
public interface ReportDBManage {
    List<CheckReports> getCheckReportsDataByDB(String visitNumber);

    Map<String, List<ReportFiles>> ensureReportsReady(List<CheckReportsEntity> httpCheckReports, List<CheckReports> dbCheckReports);

}
