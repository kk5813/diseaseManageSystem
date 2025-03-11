package com.zcc.highmyopia.hospital.repository;

import com.zcc.highmyopia.hospital.entity.*;
import com.zcc.highmyopia.po.ReportFiles;

import java.util.List;

/**
 * @ClassName ISaveToDataBase
 * @Description
 * @Author aigao
 * @Date 2025/3/10 22:04
 * @Version 1.0
 */
public interface ISaveToDataBase {
    void saveDoctorTable(List<VisitEntity> visitEntities);
    void saveSiteTable(List<VisitEntity> visitEntities);
    void saveDeptTable(List<VisitEntity> visitEntities);


    void saveByPatientID(String patientID ,PatientEntity patientEntity, List<CheckReportsEntity> checkReportsEntities);

    void saveByVisitNumber(VisitEntity visitEntity, List<ElementVisionEntity> elementVisionEntities, List<CheckResultsEntity> checkResultsEntities, List<ElementEntity> elementEntities);

    void saveRecipeAndOrderDetail(List<RecipeEntity> recipeEntities);

}
