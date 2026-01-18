package com.zcc.highmyopia.AIDiagnose.service.Impl;

import com.zcc.highmyopia.AIDiagnose.entity.request.DiagnoseRequestDTO;
import com.zcc.highmyopia.AIDiagnose.entity.request.ModelRequestVO;
import com.zcc.highmyopia.AIDiagnose.entity.response.DiagnoseResponse;
import com.zcc.highmyopia.AIDiagnose.manager.dbManager.ModelConfigDB;
import com.zcc.highmyopia.AIDiagnose.manager.dbManager.ReportDBManage;
import com.zcc.highmyopia.AIDiagnose.manager.fileFilterStrategy.FileFilterContext;
import com.zcc.highmyopia.AIDiagnose.manager.modelManager.Impl.AIModelClientImpl;
import com.zcc.highmyopia.AIDiagnose.manager.networkOperations.ThirdApiClient;
import com.zcc.highmyopia.AIDiagnose.service.AIDiagnoseService;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.exception.BusinessException;
import com.zcc.highmyopia.hospital.entity.CheckReportsEntity;
import com.zcc.highmyopia.po.CheckReports;
import com.zcc.highmyopia.po.ReportFiles;
import com.zcc.highmyopia.util.ThrowUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName AIDiagnoseServiceImpl
 * @Description
 * @Author aigao
 * @Date 2026/1/17 22:09
 * @Version 1.0
 */
@Service
@AllArgsConstructor
@Slf4j
public class AIDiagnoseServiceImpl implements AIDiagnoseService {

    private static final int ADD_DATE = 2;
    public static final String NOT_FOUND_REPORTS_MSG = "根据患者的visitNumber在该段时间(无法跨月访问数据)内未能获取到检查报告数据";
    public static final String MISSING_REPORTS_MSG = "患者缺少必要的检查项目，缺少的检查项目有：";

    private final ThirdApiClient thirdApiClient;

    private final ModelConfigDB modelConfigDB;

    private final ReportDBManage reportDBManage;

    private final FileFilterContext filterFilesContext;

    private final AIModelClientImpl aiModelClient;

    private List<String> getMissingReportTypes(List<CheckReportsEntity> getCheckReports, List<String> needCheckItems){
        Set<String> existingTypes = getCheckReports.stream().map(CheckReportsEntity::getItemName).collect(Collectors.toSet());
        return needCheckItems.stream()
                .filter(item-> !existingTypes.contains(item))
                .collect(Collectors.toList());
    }
    @Override
    public List<DiagnoseResponse> diagnose(DiagnoseRequestDTO request) throws Exception {
        // 1. Controller层完成参数检验，这里默认参数合法
        String visitNumber = request.getVisitNumber();
        Integer diseaseId = request.getDiseaseId();
        // 2. 从第三方接口获取检查报告json数据
        List<CheckReportsEntity> httpCheckReports = thirdApiClient.getCheckReportsDataByHttp(visitNumber, ADD_DATE);
        ThrowUtils.throwIf(httpCheckReports == null || httpCheckReports.isEmpty(), 4040, NOT_FOUND_REPORTS_MSG);

        // 3. 根据diseaseId获取需要诊断的检查项目
        List<String> needCheckItems = modelConfigDB.getNeedCheckItem(diseaseId);

        // 4. 判断httpCheckReports检查项目是否齐全
        List<String> httpMissingReportTypes = getMissingReportTypes(httpCheckReports, needCheckItems);
        // 4.1 如果不齐全， 抛出异常，提示缺少的检查项目
        ThrowUtils.throwIf(httpMissingReportTypes != null && !httpMissingReportTypes.isEmpty(),
                4010, MISSING_REPORTS_MSG + String.join(",", httpMissingReportTypes));
        // 4.2 如果齐全，对httpCheckReports 再进行过滤
        HashSet<String> needCheckSet = new HashSet<>(needCheckItems);
        httpCheckReports = httpCheckReports.stream()
                .filter(report -> needCheckSet.contains(report.getItemName()))
                .collect(Collectors.toList());

        // 5. 从数据库获取检查报告数据
        List<CheckReports> dbCheckReports = reportDBManage.getCheckReportsDataByDB(visitNumber);

        // 6. 确保dbCheckReports检查项目是否齐全，数据库关联的文件是否真实存在。 否则则用httpCheckReports中的链接去下载补齐，并去下载文件，写数据库，返回诊断文件列表
        // todo 这里要开启事务
        Map<String, List<ReportFiles>> ensuredReportFiles = reportDBManage.ensureReportsReady(httpCheckReports, dbCheckReports);

        // 7. 对ensuredReportFiles中的文件进行过滤，得到最终的诊断文件列表
        Map<String, List<String>> filteredReportFiles = new HashMap<>();
        for(Map.Entry<String, List<ReportFiles>> entry: ensuredReportFiles.entrySet()){
            String itemName = entry.getKey();
            Map<String,String> config = new HashMap<>();
            config.put("OCTType", Constants.OCTToType.get(String.valueOf(diseaseId)));
            List<String> reportFilePaths = filterFilesContext.executeFilter(itemName, entry.getValue(), config);
            filteredReportFiles.put(Constants.ItemNameToType.get(itemName), reportFilePaths);
        }

        // 8. 调用AI模型进行诊断，返回诊断结果
        String urlByDiseaseId = modelConfigDB.getUrlByDiseaseId(diseaseId);
        return aiModelClient.callModel(urlByDiseaseId, ModelRequestVO.builder()
                .visitNumber(visitNumber)
                .imagePaths(filteredReportFiles)
                .config(new HashMap<>())
                .build()
        );
    }

}
