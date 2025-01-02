package com.zcc.highmyopia.hospital.service.impl;

import com.alibaba.fastjson.JSON;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.hospital.entity.*;
import com.zcc.highmyopia.hospital.repository.ISaveRepository;
import com.zcc.highmyopia.hospital.service.IDownLoadService;
import com.zcc.highmyopia.hospital.utils.HttpClientUtils;
import com.zcc.highmyopia.hospital.utils.Response;
import com.zcc.highmyopia.po.ReportFiles;
import com.zcc.highmyopia.util.PDFToImg;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author zcc
 * @Date 2024/12/16
 * @Description 第三方平台获取数据的接口
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DownLoadService implements IDownLoadService {

    @Value("${hospital.hospId}")
    private String hospId;
    @Value("${hospital.appKey}")
    private String appKey;
    @Value("${hospital.appSecret}")
    private String appSecret;
    @Value("${hospital.vision-hospId}")
    private String visionHospId;
    @Value("${hospital.vision-appKey}")
    private String visionAppKey;
    @Value("${hospital.vision-appSecret}")
    private String visionAppSecret;
    @Value("${hospital.AHisHost}")
    private String AHisHost;
    @Value("${hospital.APacsHost}")
    private String APacsHost;
    @Value("${hospital.filePath}")
    private String targetPath;
    @Value("${hospital.pdf2ImgPath}")
    private String PDFToImgRelativePath;
    private final int connectTimeOut = 7200;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ISaveRepository saveRepository;

    // 患者就诊信息
    @Override
    public List<VisitEntity> getVisits(String beginData, String endData) throws Exception {
        String path = "/api/interface/medical/getPatientVisit";
        Map<String, String> query = new HashMap<>();
        query.put("diagBdate", beginData);
        query.put("diagEdate", endData);
        String reqJson = "";
        Map<String, String> headers = new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();

        Response resp = HttpClientUtils.httpPostJson(AHisHost, path, connectTimeOut, headers,
                query, reqJson, signHeaderPrefixList, appKey, appSecret, hospId);
        int statusCode = resp.getStatusCode();
        if (statusCode != 200) {
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        }
        String body = resp.getBody();
        // 解析 JSON 响应体
        List<VisitEntity> visitEntities = JSON.parseArray(
                JSON.parseObject(body).getJSONArray("data").toString(),
                VisitEntity.class);
        // 持久化
        saveRepository.saveVisits(visitEntities);
        return visitEntities;
    }

    @Override
    public void getRecipe(String beginData, String endData) throws Exception {
        String path = "/api/interface/medical/getOutpRecipe";

        Map<String, String> querys = new HashMap<>();
        querys.put("billingBdate", beginData);
        querys.put("billingEdate", endData);
        String reqJson = "";
        Map<String, String> headers = new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpPostJson(AHisHost, path, connectTimeOut, headers, querys, reqJson, signHeaderPrefixList, appKey, appSecret, hospId);
        int statusCode = resp.getStatusCode();
        if (statusCode != 200)
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        String body = resp.getBody();
        List<RecipeEntity> recipeEntities = JSON.parseArray(
                JSON.parseObject(body).getJSONArray("data").toString(),
                RecipeEntity.class);

        saveRepository.saveRecipeAndOrderDetail(recipeEntities);
    }

    // 检查结果 CheckResult
    @Override
    public void getCheckResult(String beginData, String endData, String visitNumber) throws Exception {
        String path = "/alis/interface/reportDetail/getReportDetail";
        Map<String, String> r = new HashMap<>();
        r.put("auditDateBegin", beginData);
        r.put("auditDateEnd", endData);
        r.put("visitNumber", visitNumber);
        String reqJson = JSON.toJSONString(r);
        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> query = null;
        List<String> signHeaderPrefixList = new ArrayList<>();

        Response resp = HttpClientUtils.httpPostJson(AHisHost, path, connectTimeOut, headers, query, reqJson,
                signHeaderPrefixList, appKey, appSecret, hospId);
        int statusCode = resp.getStatusCode();
        if (statusCode != 200)
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        String body = resp.getBody();
        List<CheckResultsEntity> checkResultsEntityList = JSON.parseArray(
                JSON.parseObject(body).getJSONArray("data").toString(),
                CheckResultsEntity.class);
        saveRepository.saveCheckResult(checkResultsEntityList);
    }

    @Override
    public void getCheckResult(String beginData, String endData) throws Exception {
        getCheckResult(beginData,endData, "");
    }

    @Override
    public void getCheckResult(String beginData, String endData, List<String> visitNumbers) throws Exception {
        visitNumbers.forEach(visitNumber -> {
            try {
                getCheckResult(beginData, endData, visitNumber);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    // 门诊病历，visit字段必传
    @Override
    public void getOutElementByVisitNumber(String beginData, String endData, String visitNumber) throws Exception {
        String path = "/api/aemro/outpElement/getOutpElementByCondition";

        Map<String, String> query = new HashMap<>();
        query.put("aemr_bdate", beginData);
        query.put("aemr_edate", endData);
        query.put("Visit_number", visitNumber);
        String reqJson = JSON.toJSONString(query);
        Map<String,String>headers =new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpPostJson(AHisHost, path, connectTimeOut, headers,
                query, reqJson, signHeaderPrefixList, appKey, appSecret, hospId);
        int statusCode = resp.getStatusCode();
        if (statusCode != 200)
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        String body = resp.getBody();
        ElementEntity elementEntity = JSON.parseObject(
                JSON.parseObject(body).getJSONArray("data").toString(),
                ElementEntity.class);
        saveRepository.saveElement(elementEntity);
    }

    @Override
    public void getPatientInfoByPatientId(String patientId) throws Exception {
        String path = "/api/interface/patientInfo/getById";

        Map<String, String> r = new HashMap<>();
        r.put("id", patientId);
        String reqJson = JSON.toJSONString(r);
        Map<String, String> headers = new HashMap<>();
        Map<String, String> query = null;
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpPostJson(AHisHost, path, connectTimeOut, headers,
                query, reqJson, signHeaderPrefixList, appKey, appSecret, hospId);
        int statusCode = resp.getStatusCode();
        if (statusCode != 200)
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        String body = resp.getBody();
        PatientEntity patientEntity = JSON.parseObject(
                JSON.parseObject(body).getJSONArray("data").toString(),
                PatientEntity.class);
        saveRepository.savePatientInfo(patientEntity);
    }

    @Override
    public void getPatientInfoByPatientId(List<String> patientIds) throws Exception {
        patientIds.forEach(e -> {
            try {
                getPatientInfoByPatientId(e);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @Override
    public void getElementVisionByVisitNumber(String beginData, String endData, String visitNumber) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 假设日期格式为 "yyyyMMdd"
        try {
            Date beginDate = sdf.parse(beginData);
            Date endDate = sdf.parse(endData);

            long diffInMillies = endDate.getTime() - beginDate.getTime();
            long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillies); // 将毫秒转为天数

            // 判断差值是否超过一个月
            if (diffInDays > 30) {
                throw new IllegalArgumentException("日期间隔超过一个月");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (StringUtils.isBlank(visitNumber))
            throw new AppException(400, "visitNumber字段必传");
        String path = "/avis/interface/deviceDocking/getAutoVisionByVisitNumber";

        Map<String,String> querys = new HashMap<>();
        querys.put("checkBdate",beginData);
        querys.put("checkEdate",endData);
        querys.put("visitNumber",visitNumber);

        Map<String,String>headers =new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpGet(AHisHost, path, connectTimeOut, headers, querys, signHeaderPrefixList,visionAppKey,visionAppSecret,visionHospId);
        int statusCode = resp.getStatusCode();
        if (statusCode != 200)
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        String body = resp.getBody();
        List<ElementVisionEntity> visionEntities = JSON.parseArray(
                JSON.parseObject(body).getJSONArray("data").toString(),
                ElementVisionEntity.class);
        saveRepository.saveElementVision(visionEntities);
    }
    @Override
    public void getElementVisionByVisitNumber(String beginData, String endData, List<String> visitNumber) throws Exception {
        visitNumber.forEach( entity -> {
            try {
                getElementVisionByVisitNumber(beginData,endData,entity);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void getOutElementByVisitNumber(String beginData, String endData, List<String> visitNumbers) throws Exception {
        visitNumbers.forEach(visitNumber -> {
            try {
                getOutElementByVisitNumber(beginData, endData, visitNumber);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void getCheckReport(String beginData, String endData) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); // 假设日期格式为 "yyyyMMdd"
        try {
            Date beginDate = sdf.parse(beginData);
            Date endDate = sdf.parse(endData);

            long diffInMillies = endDate.getTime() - beginDate.getTime();
            long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillies); // 将毫秒转为天数

            // 判断差值是否超过一个月
            if (diffInDays > 30) {
                throw new IllegalArgumentException("日期间隔超过一个月");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String path = "/api/report/getList";
        String url = UriComponentsBuilder.fromHttpUrl(APacsHost + path)
                .queryParam("physc_bdate", beginData)
                .queryParam("physc_edate", endData)
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        int statusCode = response.getStatusCodeValue();
        if (statusCode != 200)
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        String body = response.getBody();
        List<CheckReportsEntity> checkReportsEntities = JSON.parseArray(
                Objects.requireNonNull(JSON.parseObject(body)).getJSONArray("data").toString(),
                CheckReportsEntity.class);
        saveRepository.saveCheckReportsAndReportFiles(checkReportsEntities);
    }

    @Override
    public void getCheckReportByPatientId(String beginData, String endData, List<String> patientIds) {
        patientIds.forEach(patientId -> {
            try {
                getCheckReportByPatientId(beginData, endData, patientId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void getCheckReportByPatientId(String beginData, String endData, String patientId) {
        String path = "/api/report/getList";
        String url = UriComponentsBuilder.fromHttpUrl(APacsHost + path)
                .queryParam("physc_bdate", beginData)
                .queryParam("physc_edate", endData)
                .queryParam("patient_id", patientId)
                .toUriString();

        System.out.println("请求的URL: " + url);

        // 发送GET请求
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        int statusCode = response.getStatusCodeValue();
        if (statusCode != 200) {
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        }
        String body = response.getBody();

        List<CheckReportsEntity> checkReportsEntities = JSON.parseArray(
                Objects.requireNonNull(JSON.parseObject(body)).getJSONArray("data").toString(),
                CheckReportsEntity.class);
        saveRepository.saveCheckReportsAndReportFiles(checkReportsEntities);
    }


    @Override
    public void DownLoadReportImage(ReportFiles reportFile) {

        String urls = APacsHost + reportFile.getUrl();  // 获取文件的URL

        ResponseEntity<byte[]> response = restTemplate.getForEntity(urls, byte[].class);
        int statusCode = response.getStatusCodeValue();

        if (statusCode != 200) {
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        }

        byte[] fileBytes = response.getBody();  // 获取文件内容的字节数组

        String fileType = reportFile.getType();
        fileType = fileType.split("/")[1];

        // 从 reportFile 获取文件名，假设 reportFile 对象有 getFileName() 方法
        String fileName = UUID.randomUUID() + "." + fileType;

        File targetFile = new File(targetPath, fileName);  // 拼接文件路径和文件名

        // 确保目标目录存在
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();  // 创建目标目录
        }
        //创建新的目录单独存PDF转换后的图片
        String PDFToImgPath = targetPath + PDFToImgRelativePath;
        File destPath = new File(PDFToImgPath);
        log.info("写入路径为：" + PDFToImgPath);
        if(!destPath.exists()){
            destPath.mkdirs();
        }
        // 将文件内容写入到指定路径
        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            if (fileBytes != null) {
                fos.write(fileBytes);
                // 同时将pdf转化为image
                if(fileName.endsWith(".pdf")) {
                    log.info("保存为图片");
                    PDFToImg.pdf2Image(targetPath, PDFToImgPath, fileName, PDFToImg.PNG, PDFToImg.DPI_MID);
                }
            }
            // 更新状态为已下载
            reportFile.setIsDownLoad(1);  // 标记文件已下载
            reportFile.setFilePath(targetFile.getAbsolutePath());  // 设置文件的相对路径
            saveRepository.updateReportFiles(reportFile);  // 更新数据库
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件保存失败", e);
        }
    }

    @Override
    public void DownLoadReportImageBatch() {
        List<ReportFiles> reportFiles = saveRepository.getNotDownLoadFiles();
        reportFiles.forEach(this::DownLoadReportImage);
    }

    @Override
    public void DownLoadReportImageFunds(ReportFiles reportFile) {

        String urls = APacsHost + reportFile.getUrl();  // 获取文件的URL

        ResponseEntity<byte[]> response = restTemplate.getForEntity(urls, byte[].class);
        int statusCode = response.getStatusCodeValue();

        if (statusCode != 200) {
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        }

        byte[] fileBytes = response.getBody();  // 获取文件内容的字节数组

        String fileType = reportFile.getType();
        fileType = fileType.split("/")[1];

        // 从 reportFile 获取文件名，假设 reportFile 对象有 getFileName() 方法
        String fileName = UUID.randomUUID() + "." + fileType;

        File targetFile = new File(targetPath, fileName);  // 拼接文件路径和文件名

        // 确保目标目录存在
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();  // 创建目标目录
        }

        // 将文件内容写入到指定路径
        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            if (fileBytes != null) {
                fos.write(fileBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件保存失败", e);
        }
    }
    @Override
    public void getFunds(String beginData, String endData) throws InterruptedException {
        String path = "/api/report/getList";
        String url = UriComponentsBuilder.fromHttpUrl(APacsHost + path)
                .queryParam("physc_bdate", beginData)
                .queryParam("physc_edate", endData)
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        int statusCode = response.getStatusCodeValue();
        if (statusCode != 200)
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        String body = response.getBody();
        List<CheckReportsEntity> checkReportsEntities = JSON.parseArray(
                Objects.requireNonNull(JSON.parseObject(body)).getJSONArray("data").toString(),
                CheckReportsEntity.class);
        log.info("{}的数据以及完成保存，下面进行保存图片",beginData);
        int i = 0;
        for (CheckReportsEntity checkReportsEntity : checkReportsEntities) {
            if (checkReportsEntity.getItemName().equals("眼底照相")){
                List<ReportFiles> files = checkReportsEntity.getFiles();
                for (ReportFiles file : files) {
                    DownLoadReportImageFunds(file);
                    i++;
                }
            }
        }
        log.info("{} 数量为:{}", beginData, i);
    }
}
