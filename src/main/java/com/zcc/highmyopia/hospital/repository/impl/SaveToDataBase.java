package com.zcc.highmyopia.hospital.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.zcc.highmyopia.common.exception.BusinessException;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.hospital.entity.*;
import com.zcc.highmyopia.hospital.repository.ISaveToDataBase;
import com.zcc.highmyopia.hospital.utils.PathGenerateUtil;
import com.zcc.highmyopia.mapper.IReportFilesMapper;
import com.zcc.highmyopia.po.*;
import com.zcc.highmyopia.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zcc.highmyopia.common.lang.ResultCode.Y_saveByVisitNumber;

/**
 * @ClassName SaveToDataBase
 * @Description 只负责数据持久化
 * @Author aigao
 * @Date 2025/3/10 22:04
 * @Version 1.0
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class SaveToDataBase implements ISaveToDataBase {
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
    private final IElementVisionService elementVisionService;
    private final PathGenerateUtil pathGenerateUtil;

    private final IReportFilesMapper reportFilesMapper;
    @Override
    public void saveDoctorTable(List<VisitEntity> visitEntities) {
        if (visitEntities == null || visitEntities.isEmpty()) return;
        // 去重
        List<Doctor> doctors = new ArrayList<>(visitEntities.stream()
                .map(visitEntity -> {
                    Doctor doctor = new Doctor();
                    doctor.setId(visitEntity.getDoctorId());
                    doctor.setDoctorName(visitEntity.getDoctorName());
                    doctor.setSiteId(visitEntity.getSiteId());
                    doctor.setStatus(1);
                    return doctor;
                })
                .collect(Collectors.toMap(Doctor::getId, doctor -> doctor, (existing, replacement) -> existing))
                .values());
        /*
        * 这里单次插入数据：1.经过去重后单日的doctor 数据量不会太大
        * 2. 为doctor数据添加事务，意义不大，因为每天都会添加，即使这次失败，后续也会成功。
        * 3. Todo 基于2的逻辑，后续可以加一个判断，当数据库中的doctor数据够多，每次都批量更新。
        * 4. 后续的site, dept 基于同样的逻辑。
        * */
        try{
            doctorService.saveOrUpdateBatch(doctors);
        }catch(Exception e){
            log.error("doctor本地保存信息失败", e);
        }
//        for(Doctor doctor : doctors){
//            try{
//                doctorService.saveOrUpdate(doctor);
//            }catch(Exception e){
//                log.error("doctor本地保存信息失败", e);
//                // todo 写文件记录
//            }
//        }
    }

    @Override
    public void saveSiteTable(List<VisitEntity> visitEntities) {
        if (visitEntities == null || visitEntities.isEmpty()) return;
        List<Site> sites = new ArrayList<>(visitEntities.stream()
                // 添加过滤步骤，排除getSiteId为空的visitEntity
                .filter(visitEntity -> visitEntity.getSiteId() != null)
                .map(visitEntity -> {
                    Site site = new Site();
                    site.setId(visitEntity.getSiteId());
                    site.setSiteName(visitEntity.getSiteName());
                    site.setStatus(1);
                    return site;
                })
                .collect(Collectors.toMap(Site::getId, site -> site, (existing, replacement) -> existing))
                .values());
        try{
            siteService.saveOrUpdateBatch(sites);
        }catch(Exception e){
            log.error("site本地保存信息失败", e);
        }
//        for(Site site : sites){
//            try{
//                siteService.saveOrUpdate(site);
//            }catch(Exception e){
//                log.error("site本地保存信息失败", e);
//                // todo 写文件记录
//            }
//        }
    }

    @Override
    public void saveDeptTable(List<VisitEntity> visitEntities) {
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
        try{
            deptService.saveOrUpdateBatch(depts);
        }catch(Exception e){
            log.error("dept本地保存信息失败", e);
        }
//        for(Dept dept : depts){
//            try{
//                deptService.saveOrUpdate(dept);
//            }catch(Exception e){
//                log.error("dept本地保存信息失败", e);
//                // todo 写文件记录
//            }
//        }
    }


    /**
     * 每次保存数据只保存一个病人相关的数据，要么全部成功，要么回退。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveByPatientID(String patientID, PatientEntity patientEntity, List<CheckReportsEntity> checkReportsEntities) {
        try{
            if(patientEntity!=null){
                Patients patients = PatientEntity.entityToPo(patientEntity);
                patientsService.saveOrUpdate(patients);
            }
            if(checkReportsEntities != null && !checkReportsEntities.isEmpty()){
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
                    /*
                    *1.根据patientID 获取到报告以后，立刻根据patientID，checkTime,itemName
                    * 构建出/{病人ID}/{年}/{月}/{检查项目名称}/{文件名}
                    * */
                    files.forEach(file -> {
                        file.setReportId(reportId);
                        file.setIsDownLoad(0);
                        String type = file.getType().split("/")[1];
                        String path = pathGenerateUtil.generatePath(checkReportsEntity.getPatientId(), checkReportsEntity.getCheckTime(), checkReportsEntity.getItemName(), type);
                        file.setFilePath(path);
                    });
                    reportFilesList.addAll(files);
                }
                reportFilesService.saveOrUpdateBatch(reportFilesList);
            }
        }catch(Exception e){
            log.error("PatientID=的病人信息保存失败，涉及表patients,check_report,report_file",patientID);
            ResultCode ySaveByPatientID = ResultCode.Y_saveByPatientID;
            throw new BusinessException(ySaveByPatientID.getCode(),ySaveByPatientID.getInfo()+patientID);
        }
    }

    /**
    *  通过VisitNumber获取要保存的数据
    * */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveByVisitNumber(VisitEntity visitEntity, List<ElementVisionEntity> visionEntities, List<CheckResultsEntity> checkResultsEntityList, List<ElementEntity> elementEntities) {
        try{
            visitsService.saveOrUpdate(VisitEntity.entityToPo(visitEntity));
            saveCheckResult(checkResultsEntityList);
            saveElementVision(visionEntities);
            saveElement(elementEntities);
        }catch (BusinessException e){
            log.error(e.getMessage());
            throw e;
        }catch(Exception e){
            throw new BusinessException(ResultCode.Y_saveByVisitNumber.getCode(),ResultCode.Y_saveByVisitNumber.getInfo()+ visitEntity.getVisitNumber());
        }
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecipeAndOrderDetail(List<RecipeEntity> recipeEntities) {
        if (recipeEntities == null || recipeEntities.isEmpty()) return;
        try {
            Long doctorId = 1L;
            Long deptId = 1L;
            List<Recipe> recipes = new ArrayList<>();
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (RecipeEntity recipeEntity : recipeEntities) {
                List<OrderDetail> orderDetail = recipeEntity.getOrderDetail();
                orderDetails.addAll(orderDetail);

                /**
                 *  这里 是有一个大问题，当数据库中没有这个医生时
                 *  todo 待解决
                 */
                // 查询医生ID
                Doctor doctor = doctorService.getOne(new LambdaQueryWrapper<Doctor>()
                        .eq(Doctor::getDoctorName, recipeEntity.getDoctorName()));
                if (doctor != null)
                    doctorId = doctor.getId();
                // 查询科室ID
                Dept dept = deptService.getOne(new LambdaQueryWrapper<Dept>()
                        .eq(Dept::getDeptName, recipeEntity.getDeptName()));
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
            // 理论上不会出现id重复的情况
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
            log.error("保存处方信息失败:", e);
            throw new BusinessException(ResultCode.Y_saveRecipeAndOrderDetail);
        }
    }

    // @Transactional 要作用到非私有方法上
    @Transactional(rollbackFor = Exception.class)
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
            throw new BusinessException(ResultCode.Y_saveCheckResult);
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public void saveElementVision(List<ElementVisionEntity> visionEntities) {
        if (visionEntities == null || visionEntities.isEmpty()) return;

        try {
            List<ElementVision> elementVisions = visionEntities.stream()
                    .map(ElementVisionEntity::entityToPo).collect(Collectors.toList());
            elementVisionService.saveOrUpdateBatch(elementVisions);
        }catch (Exception e){
            throw new BusinessException(ResultCode.Y_saveElementVision);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveElement(List<ElementEntity> elementEntities) {
        if (elementEntities == null || elementEntities.isEmpty()) return;
        try{
            List<Element> elements = elementEntities.stream()
                    .map(ElementEntity::entityToPo).collect(Collectors.toList());
            elementService.saveOrUpdateBatch(elements);
        } catch (Exception e) {
            throw new BusinessException(ResultCode.Y_saveElement);
        }

    }

}
