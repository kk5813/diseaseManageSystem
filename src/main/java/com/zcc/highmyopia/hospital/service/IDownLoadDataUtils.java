package com.zcc.highmyopia.hospital.service;

import com.zcc.highmyopia.hospital.entity.*;
import com.zcc.highmyopia.po.ReportFiles;

import java.util.List;

/**
 * @ClassName IDownLoadDataUtils
 * @Description
 * @Author aigao
 * @Date 2025/3/10 21:02
 * @Version 1.0
 */
public interface IDownLoadDataUtils {
    List<VisitEntity> getVisits(String beginData, String endData) throws Exception;

    PatientEntity getPatientInfoByPatientId(String patientId) throws Exception;

    List<CheckReportsEntity> getCheckReportByPatientId(String beginData, String endData, String patientId) throws Exception;

    List<ElementVisionEntity> getElementVisionByVisitNumber(String beginData, String endData, String visitNumber) throws Exception;

    List<CheckResultsEntity> getCheckResultByVisitNumber(String beginData, String endData, String visitNumber) throws Exception;

    ElementEntity getOutElementByVisitNumber(String beginData, String endData, String visitNumber) throws Exception;

    List<RecipeEntity> getRecipe(String beginData, String endData) throws Exception;

}
