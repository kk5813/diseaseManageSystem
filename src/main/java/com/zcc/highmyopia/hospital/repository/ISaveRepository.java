package com.zcc.highmyopia.hospital.repository;

import com.zcc.highmyopia.hospital.entity.*;
import com.zcc.highmyopia.po.ReportFiles;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description 下载信息保存到数据库仓储服务
 */
public interface ISaveRepository {
    void saveVisits(List<VisitEntity> visitEntities);

    void saveRecipeAndOrderDetail(List<RecipeEntity> recipeEntity);

    void saveCheckResult(CheckResultsEntity checkResultEntity);

    void saveElement(ElementEntity elementEntity);

    void saveCheckReportsAndReportFiles(CheckReportsEntity checkReportsEntity);

    List<ReportFiles> DownLoadReportImageBatch();

    void updateReportFiles(ReportFiles reportFile);
}
