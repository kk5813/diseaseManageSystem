package com.zcc.highmyopia.hospital.repository.impl;

import com.zcc.highmyopia.hospital.entity.*;
import com.zcc.highmyopia.hospital.repository.ISaveRepository;
import com.zcc.highmyopia.mapper.*;
import com.zcc.highmyopia.po.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Slf4j
@Repository
public class SaveRepository implements ISaveRepository {

    @Resource
    private IVisitsMapper visitsMapper;

    @Resource
    private IDoctorMapper doctorMapper;

    @Resource
    private IRecipeMapper recipeMapper;

    @Resource
    private IDeptMapper deptMapper;

    @Resource
    private ICheckResultsMapper checkResultsMapper;

    @Resource
    private IElementMapper elementMapper;

    @Resource
    private ICheckReportsMapper checkReportsMapper;

    @Resource
    private IReportFilesMapper reportFilesMapper;

    @Override
    public void saveVisits(List<VisitEntity> visitEntities) {
        // 封装实体
        List<Visits> visitsList = new ArrayList<>();
        for (VisitEntity visitEntity : visitEntities) {
            Visits visits = VisitEntity.entityToPo(visitEntity);
            visitsList.add(visits);
        }
        visitsMapper.insertBatch(visitEntities);
    }

    @Override
    public void saveCheckResult(CheckResultsEntity checkResultEntity) {
        CheckResults checkResults = CheckResultsEntity.entityToPo(checkResultEntity);
        checkResultsMapper.insert(checkResults);
    }

    @Override
    public void saveRecipeAndOrderDetail(List<RecipeEntity> recipeEntities) {
        List<Recipe> recipes = new ArrayList<>();
        for (RecipeEntity recipeEntity : recipeEntities) {
            // 储存order_detail todo
            List<OrderDetail> orderDetail = recipeEntity.getOrderDetail();
            //saveRepository.saveOrderDetails
            Long orderDetailId = orderDetail.get(0).getOrderId();

            // 查询医生ID
            Long doctorId = doctorMapper.getDoctorIdByName(recipeEntity.getDoctorName()).getId();
            // 查询科室ID
            Long deptId = deptMapper.getDeptByName(recipeEntity.getDeptName()).getId();

            // 封装实体
            Recipe recipe = new Recipe();
            recipe.setId(recipeEntity.getId());
            recipe.setDoctorId(doctorId);
            recipe.setDeptId(deptId);
            recipe.setPatientId(recipeEntity.getPatientId());
            recipe.setRegNumber(recipeEntity.getRegNumber());
            recipe.setRecipeNumber(recipeEntity.getRecipeNumber());
            recipe.setRecipeType(recipeEntity.getRecipeType());
            recipe.setBillingTime(LocalDateTime.parse(recipeEntity.getBillingTime()));
            recipe.setOrderDetailId(orderDetailId);
            recipes.add(recipe);
        }
        recipeMapper.insertBatch(recipes);

    }

    @Override
    public void saveElement(ElementEntity elementEntity) {
        Element element = ElementEntity.entityToPo(elementEntity);
        elementMapper.insert(element);
    }

    @Override
    public void saveCheckReportsAndReportFiles(CheckReportsEntity checkReportsEntity) {
        CheckReports checkReports = CheckReportsEntity.entityToPo(checkReportsEntity);
        Long reportId = checkReportsMapper.insert(checkReports);  // 作为文件的字段
        List<ReportFiles> files = checkReportsEntity.getFiles();
        files.forEach(file -> {
            file.setReportId(reportId);
            file.setIsDownLoad((short) 0);
        });
        reportFilesMapper.insertBatch(files);
    }

    @Override
    public List<ReportFiles> DownLoadReportImageBatch() {
        return reportFilesMapper.getNotDownLoad();
    }

    @Override
    public void updateReportFiles(ReportFiles reportFile) {
         reportFilesMapper.updateReportFiles(reportFile);
    }
}
