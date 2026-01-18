//package com.zcc.highmyopia.AIDiagnose.service;
//
//import com.zcc.highmyopia.AIDiagnose.request.DiagnoseRequestDTO;
//import com.zcc.highmyopia.AIDiagnose.request.ModelRequestVO;
//import com.zcc.highmyopia.AIDiagnose.response.DiagnoseResponse;
//import com.zcc.highmyopia.po.CheckReports;
//import com.zcc.highmyopia.po.ReportFiles;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * @ClassName DiagnoseService
// * @Description
// * @Author aigao
// * @Date 2026/1/17 15:59
// * @Version 1.0
// */
//public interface DiagnoseService {
//    // 这个参数是http搜索多少天内的检查报告
//    public static final int ADD_DATE = 2;
//
//    public static final String NOT_FOUND_REPORTS_MSG = "患者在该段时间内（自visitNumber日期左右两天内）未能获取到检查报告数据";
//    public static final String MISSING_REPORTS_MSG = "患者缺少必要的检查项目，缺少的检查项目有：";
//
//    default List<DiagnoseResponse> diagnose(DiagnoseRequestDTO request) throws Exception {
//        // 1. Controller层完成参数检验，这里默认参数合法
//        String visitNumber = request.getVisitNumber();
//        Integer diseaseId = request.getDiseaseId();
//        // 2. 从第三方接口获取检查报告json数据
//        List<CheckReports> httpCheckReports = getCheckReportsDataByHttp(visitNumber, ADD_DATE);
//        if(httpCheckReports == null || httpCheckReports.isEmpty()){
//            throw new RuntimeException(NOT_FOUND_REPORTS_MSG);
//        }
//        // 3. 根据diseaseId获取需要诊断的检查项目
//        List<String> needCheckItems = getNeedCheckItem(diseaseId);
//
//        // 4. 判断httpCheckReports检查项目是否齐全
//        List<String> httpMissingReportTypes = getMissingReportTypes(httpCheckReports, needCheckItems);
//            // 4.1 如果不齐全， 抛出异常，提示缺少的检查项目
//        if(httpMissingReportTypes != null && !httpMissingReportTypes.isEmpty()){
//            throw new RuntimeException(MISSING_REPORTS_MSG + String.join(",", httpMissingReportTypes));
//        }
//            // 4.2 如果齐全，对httpCheckReports 再进行过滤
//        HashSet<String> needCheckSet = new HashSet<>(needCheckItems);
//        httpCheckReports = httpCheckReports.stream()
//                .filter(report -> needCheckSet.contains(report.getItemName()))
//                .collect(Collectors.toList());
//
//        // 5. 从数据库获取检查报告数据
//        List<CheckReports> dbCheckReports = getCheckReportsDataByDB(visitNumber);
//
//        // 6. 确保dbCheckReports检查项目是否齐全，数据库关联的文件是否真实存在。 否则则用httpCheckReports中的链接去下载补齐，并去下载文件，写数据库，返回诊断文件列表
//        // todo 这里要开启事务
//        Map<String, List<ReportFiles>> ensuredReportFiles = ensureReportsReady(httpCheckReports, dbCheckReports);
//
//        // 7. 对ensuredReportFiles中的文件进行过滤，得到最终的诊断文件列表
//        Map<String, List<String>> filteredReportFiles = new HashMap<>();
//        for(Map.Entry<String, List<ReportFiles>> entry: ensuredReportFiles.entrySet()){
//            String itemName = entry.getKey();
//            List<ReportFiles> reportFiles = filterFiles(entry.getValue(), itemName);
//            filteredReportFiles.put(itemName, reportFiles.stream().map(ReportFiles::getFilePath).collect(Collectors.toList()));
//        }
//
//        // 8. 调用AI模型进行诊断，返回诊断结果
//        String urlByDiseaseId = getUrlByDiseaseId(diseaseId);
//        return callModel(urlByDiseaseId, ModelRequestVO.builder()
//                .visitNumber(visitNumber)
//                .imagePaths(filteredReportFiles)
//                .config("")
//                .build()
//        );
//
//    }
//
//    List<CheckReports> getCheckReportsDataByHttp(String visitNumber, int addDate);
//
//    List<String> getNeedCheckItem(Integer diseaseId);
//
//    default List<String> getMissingReportTypes(List<CheckReports> getCheckReports, List<String> needCheckItems){
//        Set<String> existingTypes = getCheckReports.stream().map(CheckReports::getItemName).collect(Collectors.toSet());
//        return needCheckItems.stream()
//                .filter(item-> !existingTypes.contains(item))
//                .collect(Collectors.toList());
//    }
//
//    List<CheckReports> getCheckReportsDataByDB(String visitNumber);
//
//    Map<String, List<ReportFiles>> ensureReportsReady(List<CheckReports> httpCheckReports, List<CheckReports> dbCheckReports);
//
//    List<ReportFiles> filterFiles(List<ReportFiles> reportFiles, String keywords);
//
//    String getUrlByDiseaseId(Integer diseaseId);
//
//    List<DiagnoseResponse> callModel(String url, ModelRequestVO modelRequest);
//
//}
