package com.zcc.highmyopia.AIDiagnose.manager.networkOperations;

import com.zcc.highmyopia.hospital.entity.CheckReportsEntity;
import com.zcc.highmyopia.po.CheckReports;

import java.util.List;

/**
 * @ClassName ThirdApiClient
 * @Description
 * @Author aigao
 * @Date 2026/1/17 22:14
 * @Version 1.0
 */
public interface ThirdApiClient {
    List<CheckReportsEntity> getCheckReportsDataByHttp(String visitNumber, int addDate) throws Exception;
}
