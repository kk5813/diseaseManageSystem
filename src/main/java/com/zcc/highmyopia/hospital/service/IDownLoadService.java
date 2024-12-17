package com.zcc.highmyopia.hospital.service;

import com.zcc.highmyopia.hospital.entity.VisitEntity;
import com.zcc.highmyopia.po.ReportFiles;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/16
 * @Description
 */
public interface IDownLoadService {

    // 患者就诊信息
    List<VisitEntity> getPatientVisit(String beginData, String endData) throws Exception;

    // 获取处方信息
    void getRecipe(String beginData, String endData) throws Exception;

    // 获取报告信息接口
    void getReportDetail(String beginData, String endData, String visitNumber) throws Exception;
    void getReportDetail(String beginData, String endData, List<String> visitNumbers) throws Exception;

    // 门诊病历数据接口
    void getOutElementByCondition(String beginData, String endData, String visitNumber) throws Exception;
    void getOutElementByCondition(String beginData, String endData, List<String> visitNumbers) throws Exception;

    // 患者检验结果 getList
    void getCheckResult(String beginData, String endData, String visitNumber);
    void getCheckResult(String beginData, String endData, List<String> visitNumbers);

    void DownLoadReportImage(ReportFiles reportFiles);

    void DownLoadReportImageBatch();
}
