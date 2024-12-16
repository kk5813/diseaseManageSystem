package com.zcc.highmyopia.hospital.service;

/**
 * @Author zcc
 * @Date 2024/12/16
 * @Description
 */
public interface IDownLoadService {

    // 患者就诊信息
    void getPatientVisit(String beginData, String endData) throws Exception;

    // 获取处方信息
    void getRecipe(String beginData, String endData) throws Exception;

    // 获取报告信息接口
    void getReportDetail(String beginData, String endData, String visitNumber) throws Exception;

    // 门诊病历数据接口
    void getOutElementByCondition(String beginData, String endData) throws Exception;

    // 患者检验结果 getList
    void getCheckResult(String beginData, String endData, String patientId);

    void getReportImage(String url);
}
