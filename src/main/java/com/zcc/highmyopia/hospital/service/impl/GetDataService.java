package com.zcc.highmyopia.hospital.service.impl;

import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.hospital.entity.*;
import com.zcc.highmyopia.hospital.repository.ISaveRepository;
import com.zcc.highmyopia.hospital.repository.ISaveToDataBase;
import com.zcc.highmyopia.hospital.service.IDownLoadDataUtils;
import com.zcc.highmyopia.hospital.service.IDownLoadService;
import com.zcc.highmyopia.hospital.service.IGetDataService;
import com.zcc.highmyopia.hospital.utils.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author zcc
 * @Date 2024/12/31
 * @Description
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GetDataService implements IGetDataService {


    DateTimeFormatter formatterWithSplit = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatterNoSplit = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final DataDownloaderProxy dataDownloaderProxy;
    private final IDownLoadService downLoadService;
    private final ISaveRepository repository;
    private IDownLoadDataUtils downLoadDataUtils;
    private final ISaveToDataBase saveToDataBase;

    @PostConstruct
    public void init() {
        downLoadDataUtils = dataDownloaderProxy.createProxy();
    }

    // 获取前一天的日期和时间

    // Todo 这里应该有问题，因为成员变量在一开始被赋值后续并没有修改。spring的bean默认是单例模式，也就是日期始终是项目启动那天开始算。需核实。
    LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
    String yesdataSplit = yesterday.format(formatterWithSplit);
    String yesdataNoSplit = yesterday.format(formatterNoSplit);

    LocalDateTime current = LocalDateTime.now();
    String curdataSplit = current.format(formatterWithSplit);
    String curdataNoSplit = current.format(formatterNoSplit);

    // todo : 测试代码块，后面删了就可以了
//    {
//        // 定义目标日期字符串
//        String targetDate = "20241121";
//        // 创建日期格式化器
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        // 将字符串解析为 LocalDate
//        LocalDate date = LocalDate.parse(targetDate, formatter);
//        // 将 LocalDate 转换为 LocalDateTime（假设时间为 00:00:00）
//        LocalDateTime current = date.atStartOfDay();
//        // 输出结果
//        System.out.println(current);
//        yesterday = current.minusDays(1);
//        yesdataSplit = yesterday.format(formatterWithSplit);
//        curdataSplit = current.format(formatterWithSplit);
//        curdataNoSplit = current.format(formatterNoSplit);
//        yesdataNoSplit = yesterday.format(formatterNoSplit);
//    }
    @Override
    public State getDataToday() {
//        // todo : 测试时注释下就OK
        // todo : 测试时注释下就OK
//        // 定义目标日期字符串
//        String targetDate = "20241121";
//        // 创建日期格式化器
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        // 将字符串解析为 LocalDate
//        LocalDate date = LocalDate.parse(targetDate, formatter);
//        // 将 LocalDate 转换为 LocalDateTime（假设时间为 00:00:00）
//        LocalDateTime current = date.atStartOfDay();
//        // 输出结果
//        System.out.println(current);
        current = LocalDateTime.now();
        yesterday = current.minusDays(1);
        yesdataSplit = yesterday.format(formatterWithSplit);
        curdataSplit = current.format(formatterWithSplit);
        curdataNoSplit = current.format(formatterNoSplit);
        yesdataNoSplit = yesterday.format(formatterNoSplit);

        log.info("{}定时任务，每天凌晨十二点拉取前一整天的数据信息到本地库表中", current.toString());
        // 1.先下载/api/interface/medical/getPatientVisit， 获取患者就诊信息 List<VisitEntity> visits
        List<VisitEntity> visits = null;
        try {
            log.info("当天患者就诊信息下载开始");
            visits = downLoadDataUtils.getVisits(yesdataNoSplit, curdataNoSplit);
            log.info("当天患者就诊信息下载结束");
        } catch (Exception e) {
            log.error("当天患者就诊信息下载失败", e);
            return State.ONE_VISIT;
        }
        if (visits == null || visits.isEmpty()) {
            log.warn("未获取到当天的就诊信息");
            return State.SUCCESS_NOVisitData;
        }
        //1.5 存数据表doctor, dept,site
        try {
            log.info("存数据表doctor, dept,site");
            saveToDataBase.saveDoctorTable(visits);
            saveToDataBase.saveDeptTable(visits);
            saveToDataBase.saveSiteTable(visits);
        } catch (Exception e) {
            log.error("存数据表doctor, dept,site 发生异常！忽略", e);
        }

        // 1.8 根据VisitEntity 去重提取出PatientID
        List<String> patientIDList = visits.stream()
                .map(patientVisit -> String.valueOf(patientVisit.getPatientId())).distinct().collect(Collectors.toList());

        /**
         *  2. 根据VisitEntity的 visitNumber 分布去下载
         */
        log.info("根据VisitEntity的 visitNumber 分布去下载");
        List<String> visitNumberFailList = new ArrayList<>();
        saveByVisitNumber(visits, visitNumberFailList);

        /**
         * 3. 根据patientIDList的 patientID 分别去下载
         * */
        log.info("根据patientIDList的 patientID 分别去下载");
        List<String> patientIDFailList = new ArrayList<>();
        saveDataByPatientsID(patientIDList, patientIDFailList);

        //4. 下载/api/interface/medical/getOutpRecipe， 获取门诊处方信息
        log.info("获取门诊处方信息开始");
        boolean recipeFlag = saveRecipeInfo();

        //5. 将记录存入失败的patientID,visitNumber重新存取，再失败则记录日志 , Recipe_flag失败，则尝试再存取，失败记录日志
        log.info("重试机制开始");
        List<String> patientsFailAgain = new ArrayList<>();
        List<String> visitNumberFailAgain = new ArrayList<>();
        if (!(patientIDFailList == null || patientIDFailList.isEmpty())) {
            saveDataByPatientsID(patientIDFailList, patientsFailAgain);
        }
        if (!(visitNumberFailList == null || visitNumberFailList.isEmpty())) {
            List<VisitEntity> visitNews = new ArrayList<>();
            int i = 0;
            int len = visitNumberFailList.size();
            for (VisitEntity visitEntity : visits) {
                String visitNumber = visitEntity.getVisitNumber();
                if (visitNumberFailList.get(i).equals(visitNumber)) {
                    visitNews.add(visitEntity);
                    i++;
                    if (i == len) break;
                }
            }
            saveByVisitNumber(visitNews, visitNumberFailAgain);
        }
        if (recipeFlag) {
            recipeFlag = saveRecipeInfo();
        }
        // todo 这里图片下载目录需要优化，pdf转图片好像有问题
        //6. 下载图片到目录下
        /*
         *  这里改一下：
         * 1.根据patientID 获取到报告以后，立刻根据patientID，checkTime,itemName
         * 构建出/{病人ID}/{年}/{月}/{检查项目名称}/{文件名}
         * */
        try {
            log.info("图片批量下载开始");
            downLoadDataUtils.DownLoadReportImageBatch();
        } catch (Exception e) {
            log.error("批量导入图片到本地发生异常", e);
        }

        //todo 这里更稳妥的做法是 存一个文件或者数据库记录，方便后续再度重试
        //7. 记录当天下载完成日志信息。
        if (!visitNumberFailAgain.isEmpty()) {
            // 记录日志：就诊编号重新存储失败
            log.error("就诊编号重新存储失败: {}", visitNumberFailAgain);
        }
        if (!patientsFailAgain.isEmpty()) {
            // 记录日志：患者ID重新存储失败
            log.error("患者ID重新存储失败: {}", patientsFailAgain);
        }
        if (!recipeFlag) {
            // 记录日志：Recipe_flag存储失败
            log.error("Recipe_flag存储失败");
        }
        log.info("{}下载完成", LocalDateTime.now().toString());

        // todo 后续改进：从并发角度，1.x 必须先指向完， 2，3，4可以并行执行， 5需要等待2，3，4执行完， 6需要等待5执行完, 最后打7日志

        //State状态可以优化，记录执行的更多状态信息。
        return State.SUCCESS;
    }


    /**
     * 3. 根据patientIDList的 patientID 分别去下载
     * /api/interface/patientInfo/getById 、:8083/api/report/getList
     * 获取当前ID下的 PatientEntity patientEntity ，List<CheckReportsEntity> checkReportsEntities
     * 存数据库表  patients, check_report, report_files
     * 记录失败的patientID
     */
    public void saveDataByPatientsID(List<String> patientIDList, List<String> patientIDFailList) {
        String patientID = null;

        for (String patientid : patientIDList) {
            try {
                patientID = patientid;
                if (!StringUtils.isNotBlank(patientID)) {
                    continue;
                }
                PatientEntity patientEntity = null;
                List<CheckReportsEntity> checkReportsEntities = null;
                boolean flag = true;
                try {
                    patientEntity = downLoadDataUtils.getPatientInfoByPatientId(patientID);
                    checkReportsEntities = downLoadDataUtils.getCheckReportByPatientId(yesdataSplit, curdataSplit, patientID);
                } catch (Exception e) {
                    log.error("在获取敏感信息、检查图片时，patientID={}数据下载失败", patientID, e);
                    patientIDFailList.add(patientID);
                    flag = false;
                }
                if (flag) {
                    saveToDataBase.saveByPatientID(patientID, patientEntity, checkReportsEntities);
                }
            } catch (Exception e) {
                log.error("存patientID数据发生错误{}", e.getMessage());
                if (StringUtils.isNotBlank(patientID)) {
                    patientIDFailList.add(patientID);
                }
            }
        }
    }

    /**
     * 2. 根据VisitEntity的 visitNumber 分布去下载
     * /alis/interface/reportDetail/getReportDetail、
     * /external-api/avis/interface/deviceDocking/getAutoVisionByVisitNumber、
     * /api/aemro/outpElement/getOutpElementByCondition
     * 获取当前visitNumber 下的List<ElementVisionEntity> elementVisionEntities,
     * List<CheckResultsEntity> checkResultsEntities, List<ElementEntity> elementEntities
     * 存数据库表visits, check_results, element,element_vision
     * 记录失败的visitNumber
     */
    public void saveByVisitNumber(List<VisitEntity> visits, List<String> visitNumberFailList) {
        String visitNum = null;
            for (VisitEntity visitEntity : visits) {
                try {
                visitNum = visitEntity.getVisitNumber();
                if (!StringUtils.isNotBlank(visitNum)) {
                    continue;
                }
                ElementEntity element = null;
                List<ElementVisionEntity> elementVisionEntities = null;
                List<CheckResultsEntity> checkResultsEntities = null;
                boolean flag = true;
                try {
                    element = downLoadDataUtils.getOutElementByVisitNumber(yesdataSplit, curdataSplit, visitNum);
                    elementVisionEntities = downLoadDataUtils.getElementVisionByVisitNumber(yesdataSplit, curdataSplit, visitNum);
                    checkResultsEntities = downLoadDataUtils.getCheckResultByVisitNumber(yesdataSplit, curdataSplit, visitNum);
                } catch (Exception e) {
                    log.error("在获取检查结果（文字）, 门诊病例，视力眼压时，visitNumber={}数据下载失败", visitNum, e);
                    visitNumberFailList.add(visitNum);
                    flag = false;
                }
                if (flag) {
                    ArrayList<ElementEntity> elementEntities = new ArrayList<>();
                    elementEntities.add(element);
                    saveToDataBase.saveByVisitNumber(visitEntity, elementVisionEntities, checkResultsEntities, elementEntities);
                }
                } catch (Exception e) {
                    log.error(String.valueOf(e));
                    if (StringUtils.isNotBlank(visitNum)) {
                        log.error("存入visitNumber={}数据发生错误", visitNum);
                        visitNumberFailList.add(visitNum);
                    }
                }
            }

    }

    public boolean saveRecipeInfo() {
        List<RecipeEntity> recipeEntities = null;
        boolean recipeFlag = true;
        try {
            recipeEntities = downLoadDataUtils.getRecipe(yesdataNoSplit, curdataNoSplit);
        } catch (Exception e) {
            log.error("当天患者门诊处方信息下载失败", e);
            recipeFlag = false;
        }
        //4.5 存数据库表recipe，order_detail, 记录失败与成功标志Recipe_flag
        if (recipeFlag) {
            if (recipeEntities == null || recipeEntities.isEmpty()) {
                log.warn("当天未获取到患者门诊处方信息");
            } else {
                try {
                    saveToDataBase.saveRecipeAndOrderDetail(recipeEntities);
                } catch (Exception e) {
                    log.error("存数据库表recipe，order_detail，发生异常！", e);
                    recipeFlag = false;
                }
            }
        }
        return recipeFlag;
    }

    @Override
    public void getTodayData() {
        log.info("定时任务，每天凌晨十二点拉取前一整天的数据信息到本地库表中");
        try {
            // 1. 下载所有的当天就诊信息
            List<VisitEntity> visits = downLoadService.getVisits(yesdataNoSplit, yesdataNoSplit);
            if (visits == null || visits.isEmpty()) {
                log.warn("未获取到当天的就诊信息");
                return;
            }
            List<String> visitNumbersList = visits.stream()
                    .map(VisitEntity::getVisitNumber).distinct().collect(Collectors.toList());
            List<String> patientIdsList = visits.stream()
                    .map(patientVisit -> String.valueOf(patientVisit.getPatientId())).distinct().collect(Collectors.toList());

            // 2.下载病人信息
            downLoadService.getPatientInfoByPatientId(patientIdsList);

            // 3. 获取当天的所有处方信息
            downLoadService.getRecipe(yesdataNoSplit, yesdataNoSplit);

            // 4.下载检查报告信息
            downLoadService.getCheckReportByPatientId(yesdataSplit, yesdataSplit, visitNumbersList);

            // 5.下载门诊病历信息
            downLoadService.getOutElementByVisitNumber(yesdataSplit, yesdataSplit, visitNumbersList);

            // 6.下载检验结果
            downLoadService.getCheckResult(yesdataSplit, yesdataSplit);

            // 7.下载视力眼压
            downLoadService.getElementVisionByVisitNumber(yesdataSplit, yesdataSplit, patientIdsList);

            // 8. 批量下载图片到本地位置
            downLoadService.DownLoadReportImageBatch();
            log.info("每日定時任務完成");
        } catch (Exception e) {
            log.error("定时任务执行失败，错误信息: {}", e.getMessage(), e);
            throw new AppException(500, "定时任务执行失败: " + e.getMessage());
        }
    }

    @Override
    public void getDataTest(String beginData, String endData) {
        String beginDataSplit = beginData.substring(0, 4) + "-" + beginData.substring(4, 6) + "-" + beginData.substring(6);
        String endDataSplit = endData.substring(0, 4) + "-" + endData.substring(4, 6) + "-" + endData.substring(6);

        log.info("定时任务，每天凌晨十二点拉取前一整天的数据信息到本地库表中");
        try {
            // 1. 下载所有的当天就诊信息
            List<VisitEntity> visits = downLoadService.getVisits(beginData, endData);
            if (visits == null || visits.isEmpty()) {
                log.warn("未获取到当天的就诊信息");
                return;
            }
            List<String> visitNumbersList = visits.stream()
                    .map(VisitEntity::getVisitNumber).distinct().collect(Collectors.toList());
            List<String> patientIdsList = visits.stream()
                    .map(patientVisit -> String.valueOf(patientVisit.getPatientId())).distinct().collect(Collectors.toList());

            // 2.下载病人信息
            downLoadService.getPatientInfoByPatientId(patientIdsList);

            // 3. 获取当天的所有处方信息
            downLoadService.getRecipe(beginData, beginData);

            // 4.下载检查报告信息
            downLoadService.getCheckReportByPatientId(beginData, beginData, patientIdsList);

            // 5.下载门诊病历信息
            downLoadService.getOutElementByVisitNumber(beginDataSplit, endDataSplit, visitNumbersList);

            // 6.下载检验结果
            downLoadService.getCheckResult(beginDataSplit, endDataSplit);

            // 7.下载视力眼压
            downLoadService.getElementVisionByVisitNumber(beginData, beginData, visitNumbersList);

            // 8. 批量下载图片到本地位置
            downLoadService.DownLoadReportImageBatch();
            log.info("每日定時任務完成");
        } catch (Exception e) {
            log.error("定时任务执行失败，错误信息: {}", e.getMessage(), e);
            throw new AppException(500, "定时任务执行失败: " + e.getMessage());
        }
    }


}
