package com.zcc.highmyopia.hospital.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.hospital.entity.*;
import com.zcc.highmyopia.hospital.repository.ISaveRepository;
import com.zcc.highmyopia.mapper.*;
import com.zcc.highmyopia.po.*;
import com.zcc.highmyopia.service.*;
import com.zcc.highmyopia.service.impl.DoctorService;
import com.zcc.highmyopia.service.impl.SiteService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SaveRepository implements ISaveRepository {

    private final IRecipeService recipeService;
    private final ICheckResultsService checkResultsService;
    private final IElementService elementService;
    private final ICheckReportsService checkReportsService;
    private final IReportFilesService reportFilesService;
    private final IVisitsService visitsService;
    private final IDoctorService doctorService;
    private final ISiteService siteService;
    private final IDeptService deptService;
    private final TransactionTemplate transactionTemplate;
    private final IPatientsService patientsService;
    private final IOrderDetailService orderDetailService;

    private final IReportFilesMapper reportFilesMapper;

    @Override
    public void saveVisits(List<VisitEntity> visitEntities) {
        if (visitEntities == null || visitEntities.isEmpty())
            return;
        // 封装实体
        List<Visits> visitsList = new ArrayList<>();
        // 去重
        List<Doctor> doctors = new ArrayList<>(visitEntities.stream()
                .map(visitEntity -> {
                    Visits visits = VisitEntity.entityToPo(visitEntity);
                    visitsList.add(visits);

                    Doctor doctor = new Doctor();
                    doctor.setId(visitEntity.getDoctorId());
                    doctor.setDoctorName(visitEntity.getDoctorName());
                    doctor.setSiteId(visitEntity.getSiteId());
                    doctor.setStatus(1);
                    return doctor;
                })
                .collect(Collectors.toMap(Doctor::getId, doctor -> doctor, (existing, replacement) -> existing))
                .values());

        List<Site> sites = new ArrayList<>(visitEntities.stream()
                .map(visitEntity -> {
                    Site site = new Site();
                    site.setId(visitEntity.getSiteId());
                    site.setSiteName(visitEntity.getSiteName());
                    site.setStatus(1);
                    return site;
                })
                .collect(Collectors.toMap(Site::getId, site -> site, (existing, replacement) -> existing))
                .values());

        List<Dept> depts = new ArrayList<>(visitEntities.stream()
                .map(visitEntity -> {
                    Dept dept = new Dept();
                    dept.setId(visitEntity.getDeptId());
                    dept.setDeptName(visitEntity.getDeptName());
                    dept.setStatus(1);
                    return dept;
                })
                .collect(Collectors.toMap(Dept::getId, dept -> dept, (existing, replacement) -> existing))
                .values());

        transactionTemplate.execute(status -> {
            try {
                visitsService.saveOrUpdateBatch(visitsList);
                doctorService.saveOrUpdateBatch(doctors);
                siteService.saveOrUpdateBatch(sites);
                deptService.saveOrUpdateBatch(depts);

            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("本地保存就诊信息失败", e);
                throw new RuntimeException("保存就诊信息失败", e);
            }
            return null;
        });
    }

    @Override
    public void saveRecipeAndOrderDetail(List<RecipeEntity> recipeEntities) {
        if (recipeEntities == null || recipeEntities.isEmpty()) return;

        transactionTemplate.execute(status -> {
            try {
                Long doctorId = 1L;
                Long deptId = 1L;
                List<Recipe> recipes = new ArrayList<>();
                List<OrderDetail> orderDetails = new ArrayList<>();
                for (RecipeEntity recipeEntity : recipeEntities) {
                    List<OrderDetail> orderDetail = recipeEntity.getOrderDetail();
                    orderDetails.addAll(orderDetail);

                    // 查询医生ID
                    Doctor doctor = doctorService.getOne(new LambdaQueryWrapper<Doctor>()
                            .eq(Doctor::getDoctorName, recipeEntity.getDoctorName()));
                    // todo 实际使用时放开
//                    if (doctor == null)
//                        throw new AppException(501, "数据库查无此医生");
                    if (doctor != null)
                        doctorId = doctor.getId();
                    // 查询科室ID
                    Dept dept = deptService.getOne(new LambdaQueryWrapper<Dept>()
                            .eq(Dept::getDeptName, recipeEntity.getDeptName()));
                    // todo 实际使用时放开
//                    if (dept == null)
//                        throw new AppException(501, "数据库查无此科室");
                    if (dept != null)
                        deptId = dept.getId();
                    // 封装实体
                    Recipe recipe = new Recipe();
                    recipe.setId(recipeEntity.getId());
                    recipe.setDoctorId(doctorId);
                    recipe.setDeptId(deptId);
                    recipe.setPatientId(recipeEntity.getPatientId());
                    recipe.setRegNumber(recipeEntity.getRegNumber());
                    recipe.setRecipeNumber(recipeEntity.getRecipeNumber());
                    recipe.setRecipeType(recipeEntity.getRecipeType());

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    recipe.setBillingTime(LocalDateTime.parse(recipeEntity.getBillingTime(), formatter));

                    recipes.add(recipe);
                }
                orderDetails = new ArrayList<>(orderDetails.stream()
                        .filter(orderDetail -> orderDetail.getId() != null)
                        .collect(Collectors.toMap(OrderDetail::getId, orderDetail-> orderDetail, (existing, replacement) -> existing))
                        .values());
                recipes = new ArrayList<>(recipes.stream()
                        .collect(Collectors.toMap(Recipe::getId, recipe -> recipe, (existing, replacement) -> existing))
                        .values());
                orderDetailService.saveOrUpdateBatch(orderDetails);
                recipeService.saveOrUpdateBatch(recipes);

            } catch (Exception e) {
                status.setRollbackOnly();
                log.error("保存处方信息失败:", e);
                throw new RuntimeException("保存处方信息失败", e);
            }
            return null;
        });
    }

    @Override
    public void saveCheckResult(List<CheckResultsEntity> checkResultsEntityList) {
        if (checkResultsEntityList == null || checkResultsEntityList.isEmpty()) return;
        List<CheckResults> checkResultsList = new ArrayList<>();
        for (CheckResultsEntity checkResultsEntity : checkResultsEntityList) {
            CheckResults checkResults = CheckResultsEntity.entityToPo(checkResultsEntity);
            checkResultsList.add(checkResults);
        }
        try {
            checkResultsList = new ArrayList<>(checkResultsList.stream()
                    .collect(Collectors.toMap(CheckResults::getId, checkResults -> checkResults, (existing, replacement) -> existing))
                    .values());

            checkResultsService.saveOrUpdateBatch(checkResultsList);
        } catch (Exception e) {
            log.error("保存检查结果失败", e);
            throw new RuntimeException("保存检查结果失败", e);
        }
    }

    @Override
    public void saveElement(ElementEntity elementEntity) {
        Element element = ElementEntity.entityToPo(elementEntity);
        try {
            elementService.saveOrUpdate(element);
        } catch (Exception e) {
            log.error("保存门诊病历失败", e);
            throw new RuntimeException("保存门诊病历失败", e);
        }

    }

    @Override
    public void saveCheckReportsAndReportFiles(CheckReportsEntity checkReportsEntity) {
        transactionTemplate.execute(status -> {
           try {
               CheckReports checkReports = CheckReportsEntity.entityToPo(checkReportsEntity);
               Long reportId = checkReports.getId();
               List<ReportFiles> files = checkReportsEntity.getFiles();
               files.forEach(file -> {
                   file.setReportId(reportId);
                   file.setIsDownLoad(0);
               });
               reportFilesService.saveOrUpdateBatch(files);
           }catch (Exception e){
               status.setRollbackOnly();
               log.error("保存检查报告失败:", e);
               throw new RuntimeException("保存检查报告失败", e);
           }
            return null;
        });

    }

    @Override
    public void saveCheckReportsAndReportFiles(List<CheckReportsEntity> checkReportsEntities) {
        transactionTemplate.execute(status -> {
            try {
                List<CheckReports> checkReportsList = new ArrayList<>();
                List<ReportFiles> reportFilesList = new ArrayList<>();
                for (CheckReportsEntity checkReportsEntity : checkReportsEntities) {
                    CheckReports checkReports = CheckReportsEntity.entityToPo(checkReportsEntity);
                    checkReportsList.add(checkReports);
                }
                // 报告已存
                checkReportsService.saveOrUpdateBatch(checkReportsList);
                for (int i = 0; i < checkReportsEntities.size(); i++) {
                    CheckReportsEntity checkReportsEntity = checkReportsEntities.get(i);
                    CheckReports checkReports = checkReportsList.get(i);

                    Long reportId = checkReports.getId();
                    List<ReportFiles> files = checkReportsEntity.getFiles();
                    files.forEach(file -> {
                        file.setReportId(reportId);
                        file.setIsDownLoad(0);
                    });
                    reportFilesList.addAll(files);
                }
                reportFilesService.saveOrUpdateBatch(reportFilesList);
            }catch (Exception e){
                status.setRollbackOnly();
                log.error("保存检查报告失败:", e);
                throw new RuntimeException("保存检查报告失败", e);
            }
            return null;
        });
    }


@Override
public List<ReportFiles> getNotDownLoadFiles() {
    return reportFilesMapper.getNotDownLoad();
}

@Override
public void updateReportFiles(ReportFiles reportFile) {
        reportFilesMapper.update(reportFile,
                new LambdaUpdateWrapper<ReportFiles>()
                        .eq(ReportFiles::getId, reportFile.getId())
                        .set(ReportFiles::getIsDownLoad, reportFile.getIsDownLoad())
                        .set(ReportFiles::getFilePath, reportFile.getFilePath()));
}

@Override
public void savePatientInfo(PatientEntity patientEntity) {
    Patients patients = PatientEntity.entityToPo(patientEntity);
    try {
        patientsService.saveOrUpdate(patients);
    } catch (Exception e) {
        log.error("保存用户信息失败", e);
        throw new RuntimeException("保存用户信息失败", e);
    }
}
}
