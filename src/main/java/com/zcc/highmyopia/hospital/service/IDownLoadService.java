package com.zcc.highmyopia.hospital.service;

import com.zcc.highmyopia.hospital.entity.VisitEntity;
import com.zcc.highmyopia.po.ReportFiles;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Author zcc
 * @Date 2024/12/16
 * @Description
 */
public abstract interface IDownLoadService {

    // 患者就诊信息
    List<VisitEntity> getPatientVisit(String beginData, String endData) throws Exception;

    // 获取处方信息
    void getRecipe(String beginData, String endData) throws Exception;

    // 获取检验结果接口
    void getReportDetail(String beginData, String endData) throws Exception;
    void getReportDetail(String beginData, String endData, String visitNumber) throws Exception;
    void getReportDetail(String beginData, String endData, List<String> visitNumbers) throws Exception;

    // 门诊病历数据接口
    void getOutElementByCondition(String beginData, String endData, String visitNumber) throws Exception;
    void getOutElementByCondition(String beginData, String endData, List<String> visitNumbers) throws Exception;

    // 患者检验结果 getList
    void getCheckResult(String beginData, String endData);
    void getCheckResult(String beginData, String endData, String patientId);
    void getCheckResult(String beginData, String endData, List<String> patientIds);

    void getPatientInfo(String number) throws Exception;

    // 视力眼压接口
    void getElementVision(String beginData, String endData, String visitNumber) throws Exception;
    void getElementVision(String beginData, String endData, List<String> visitNumber) throws Exception;

    void DownLoadReportImage(ReportFiles reportFiles);

    void DownLoadReportImageBatch();


    void DownLoadReportImageFunds(ReportFiles reportFile);
    // 获取眼底照相的图片
    void getFunds(String beginData, String endData) throws InterruptedException;

}
