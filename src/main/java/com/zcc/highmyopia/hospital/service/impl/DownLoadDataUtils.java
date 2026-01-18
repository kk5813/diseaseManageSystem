package com.zcc.highmyopia.hospital.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.common.exception.BusinessException;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.hospital.entity.*;
import com.zcc.highmyopia.hospital.repository.ISaveRepository;
import com.zcc.highmyopia.hospital.repository.ISaveToDataBase;
import com.zcc.highmyopia.hospital.service.IDownLoadDataUtils;
import com.zcc.highmyopia.hospital.utils.HttpClientUtils;
import com.zcc.highmyopia.hospital.utils.HttpMethod;
import com.zcc.highmyopia.hospital.utils.Response;
import com.zcc.highmyopia.mapper.IReportFilesMapper;
import com.zcc.highmyopia.po.ReportFiles;
import com.zcc.highmyopia.service.IReportFilesService;
import com.zcc.highmyopia.util.PDFToImg;
import com.zcc.highmyopia.util.ThrowUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName DownLoadDataUtil
 * @Description  这个类只赋值下载数据。
 * @Author aigao
 * @Date 2025/3/10 21:02
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DownLoadDataUtils implements IDownLoadDataUtils {
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

    //获取患者的就诊信息的接口，最先开始请求的接口
    @Override
    @Retryable(value = {SocketTimeoutException.class}, maxAttempts = 3)
    public List<VisitEntity> getVisits(String beginData, String endData) throws Exception {
        String path = "/api/interface/medical/getPatientVisit";
        Map<String, String> query = new HashMap<>();
        query.put("diagBdate", beginData);
        query.put("diagEdate", endData);
        String reqJson = "";
        Map<String, String> headers = new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        log.info("患者就诊信息下载请求前");
        Response resp = HttpClientUtils.httpPostJson(AHisHost, path, connectTimeOut, headers,
                query, reqJson, signHeaderPrefixList, appKey, appSecret, hospId);
        log.info("患者就诊信息下载请求后");
        int statusCode = resp.getStatusCode();

        ThrowUtils.throwIf(statusCode !=200, ResultCode.Z_getPatientVisit);
        String body = resp.getBody();
        // 解析 JSON 响应体
        List<VisitEntity> visitEntities = JSON.parseArray(
                JSON.parseObject(body).getJSONArray("data").toString(),
                VisitEntity.class);
        return visitEntities;
    }

    // 获取病人敏感信息的接口，电话，这种
    @Override
    @Retryable(value = {SocketTimeoutException.class}, maxAttempts = 3)
    public PatientEntity getPatientInfoByPatientId(String patientId) throws Exception {
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
        ThrowUtils.throwIf(statusCode !=200, ResultCode.Z_getById);
        String body = resp.getBody();
        PatientEntity patientEntity = JSON.parseObject(
                JSON.parseObject(body).getJSONArray("data").toString(),
                PatientEntity.class);
        return patientEntity;
    }

    // 获取图片报告的那个接口，内外访问的接口
    @Override
    @Retryable(value = {SocketTimeoutException.class}, maxAttempts = 3)
    public List<CheckReportsEntity> getCheckReportByPatientId(String beginData, String endData, String patientId) throws Exception {
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
        ThrowUtils.throwIf(statusCode !=200, ResultCode.Z_getList);
        String body = response.getBody();
        List<CheckReportsEntity> checkReportsEntities = JSON.parseArray(
                Objects.requireNonNull(JSON.parseObject(body)).getJSONArray("data").toString(),
                CheckReportsEntity.class);
        return checkReportsEntities;
    }

    @Override
    public List<CheckReportsEntity> getCheckReportByVisitNumberNew(String beginData, String endData, String visitNumber) throws Exception {
        String path = "/api/report/getList";
        String url = UriComponentsBuilder.fromHttpUrl(APacsHost + path)
                .queryParam("physc_bdate", beginData)
                .queryParam("physc_edate", endData)
                .queryParam("visit_number", visitNumber)
                .toUriString();

        System.out.println("请求的URL: " + url);

        // 发送GET请求
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        int statusCode = response.getStatusCodeValue();
        ThrowUtils.throwIf(statusCode !=200, ResultCode.Z_getCheckReportByVisitNumberNew);
        String body = response.getBody();
        List<CheckReportsEntity> checkReportsEntities = JSON.parseArray(
                Objects.requireNonNull(JSON.parseObject(body)).getJSONArray("data").toString(),
                CheckReportsEntity.class);
        return checkReportsEntities;
    }

    // 获取视力眼压的接口
    @Override
    @Retryable(value = {SocketTimeoutException.class}, maxAttempts = 3)
    public List<ElementVisionEntity> getElementVisionByVisitNumber(String beginData, String endData, String visitNumber) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 假设日期格式为 "yyyyMMdd"
        try {
            Date beginDate = sdf.parse(beginData);
            Date endDate = sdf.parse(endData);

            long diffInMillies = endDate.getTime() - beginDate.getTime();
            long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillies); // 将毫秒转为天数
            // 判断差值是否超过一个月
            if (diffInDays > 30) {
                throw new IllegalArgumentException(String.format("{}方法的参数{}和{}日期间隔超过一个月","getElementVisionByVisitNumber",beginData,endData));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String path = "/avis/interface/deviceDocking/getAutoVisionByVisitNumber";
        Map<String,String> querys = new HashMap<>();
        querys.put("checkBdate",beginData);
        querys.put("checkEdate",endData);
        querys.put("visitNumber",visitNumber);

        Map<String,String>headers =new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpGet(AHisHost, path, connectTimeOut, headers, querys, signHeaderPrefixList,visionAppKey,visionAppSecret,visionHospId);
        int statusCode = resp.getStatusCode();
        ThrowUtils.throwIf(statusCode !=200, ResultCode.Z_getAutoVisionByVisitNumber);
        String body = resp.getBody();
        List<ElementVisionEntity> visionEntities = JSON.parseArray(
                JSON.parseObject(body).getJSONArray("data").toString(),
                ElementVisionEntity.class);
        return visionEntities;
    }

    // 获取检查结果报告（文字）
    @Override
    @Retryable(value = {SocketTimeoutException.class}, maxAttempts = 3)
    public List<CheckResultsEntity> getCheckResultByVisitNumber(String beginData, String endData, String visitNumber) throws Exception {
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
        ThrowUtils.throwIf(statusCode !=200, ResultCode.Z_getReportDetail);
        String body = resp.getBody();
        System.out.println(resp.getBody().toString());
        List<CheckResultsEntity> checkResultsEntityList = JSON.parseArray(
                JSON.parseObject(body).getJSONArray("data").toString(),
                CheckResultsEntity.class);
        return checkResultsEntityList;
    }

    // 门诊病历，visit字段必传
    @Override
    @Retryable(value = {SocketTimeoutException.class}, maxAttempts = 3)
    public ElementEntity getOutElementByVisitNumber(String beginData, String endData, String visitNumber) throws Exception {
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
        ThrowUtils.throwIf(statusCode !=200, ResultCode.Z_getOutpElementByCondition);
        String body = resp.getBody();
        ElementEntity elementEntity = JSON.parseObject(
                JSON.parseObject(body).getJSONArray("data").toString(),
                ElementEntity.class);
        return elementEntity;
    }


    // 门诊处方信息
    @Override
    @Retryable(value = {SocketTimeoutException.class}, maxAttempts = 3)
    public List<RecipeEntity> getRecipe(String beginData, String endData) throws Exception {
        String path = "/api/interface/medical/getOutpRecipe";

        Map<String, String> querys = new HashMap<>();
        querys.put("billingBdate", beginData);
        querys.put("billingEdate", endData);
        String reqJson = "";
        Map<String, String> headers = new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpPostJson(AHisHost, path, connectTimeOut, headers, querys, reqJson, signHeaderPrefixList, appKey, appSecret, hospId);
        int statusCode = resp.getStatusCode();
        ThrowUtils.throwIf(statusCode !=200, ResultCode.Z_getOutpRecipe);
        String body = resp.getBody();
        List<RecipeEntity> recipeEntities = JSON.parseArray(
                JSON.parseObject(body).getJSONArray("data").toString(),
                RecipeEntity.class);
        return recipeEntities;
    }

    private final ISaveToDataBase saveToDataBase;
    @Override
    public void DownLoadReportImageBatch() {
        List<ReportFiles> reportFiles = saveToDataBase.getNotDownLoadFiles();
        reportFiles.forEach(reportFile->{
            try{
                DownLoadReportImage(reportFile);
            }catch(Exception e){
                log.error("Error downloading report image for file: {}", reportFile);
                e.printStackTrace();
            }
        });
    }

    @Override
    public void DownLoadReportImageBatchByVisitNumber(String visitNumber) throws Exception {
        List<ReportFiles> reportFiles = saveToDataBase.getNotDownLoadFilesByVisitNumber(visitNumber);
        reportFiles.forEach(reportFile->{
            try{
                DownLoadReportImage(reportFile);
            }catch(Exception e){
                log.error("Error downloading report image for file: {}", reportFile);
                e.printStackTrace();
            }
        });
    }

    @Override
    public void DownLoadReportImage(ReportFiles reportFile) {

        String urls = APacsHost + reportFile.getUrl();  // 获取文件的URL

        ResponseEntity<byte[]> response = restTemplate.getForEntity(urls, byte[].class);
        int statusCode = response.getStatusCodeValue();
        ThrowUtils.throwIf(statusCode != 200, new BusinessException(ResultCode.Z_gdownloadPage));
        byte[] fileBytes = response.getBody();  // 获取文件内容的字节数组

        String path = reportFile.getFilePath();
        File targetFile = new File(path);
        // 确保目标目录存在
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();  // 创建目标目录
        }
        // 将文件内容写入到指定路径, 默认行为：覆盖文件， 故不考虑图片已在的情况
        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
            if (fileBytes != null) {
                fos.write(fileBytes);
            }
            // 更新状态为已下载
            reportFile.setIsDownLoad(1);  // 标记文件已下载
            saveToDataBase.updateReportFiles(reportFile);  // 更新数据库
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件保存失败",e);
            //throw new RuntimeException("文件保存失败", e);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 重写DownLoadReportImage的逻辑
    @Override
    public void processReportFileDownload(List<ReportFiles> reportFiles) {
        for(ReportFiles reportFile: reportFiles){
            String urls = APacsHost + reportFile.getUrl();  // 获取文件的URL
            String targetPath = reportFile.getFilePath();
            try {
                // 步骤 A: 下载文件到磁盘 (耗时操作，无数据库交互)
                downloadFileToDisk(urls, targetPath);
                // 步骤 B: 更新数据库状态 (快速操作，开启事务)
                updateFileStatusInDb(reportFile);
            } catch (Exception e) {
                // 捕获所有异常，记录日志，但不中断主流程（如果是批量下载场景）
                log.error("处理文件下载失败: fileId={}, url={}", reportFile.getId(), urls, e);
                // 可以在这里做一些补偿机制，比如删除下载了一半的残损文件
                deleteQuietly(new File(targetPath));
            }
        }
    }

    /**
     * 2. 【原子操作】纯粹的文件下载逻辑
     * 只负责把网络流写入磁盘，不涉及任何数据库操作
     */
    private void downloadFileToDisk(String urls, String localPath) {
        // 使用 Resource 配合 copy，或者流式处理，比 byte[] 更省内存
        // 这里沿用 RestTemplate，但建议使用 execute 方法进行流式下载防 OOM
        File targetFile = new File(localPath);

        // 确保目录存在
        if (!targetFile.getParentFile().exists()) {
            boolean mkdirs = targetFile.getParentFile().mkdirs();
            if (!mkdirs) {
                throw new BusinessException(4050, "无法创建目录: " + targetFile.getParent());
            }
        }
        restTemplate.execute(
                urls,
                org.springframework.http.HttpMethod.valueOf(HttpMethod.GET),
                null,
                // 【关键】在这里加上 (ClientHttpResponse response)
                (ClientHttpResponse response) -> {
                    // 现在输入 response. 应该会有提示了
                    if (response.getStatusCode() != HttpStatus.OK) {
                        throw new BusinessException(ResultCode.Z_gdownloadPage);
                    }

                    File finaltargetFile = new File(localPath);

                    // 这里的 try-with-resources 依然只负责文件流
                    try (FileOutputStream out = new FileOutputStream(finaltargetFile)) {
                        StreamUtils.copy(response.getBody(), out);
                    } catch (IOException e) {
                        throw new BusinessException(4010, "写入失败");
                    }
                    return null;
                }
        );
        log.info("文件下载成功: {}", localPath);
    }
    /**
     * 3. 【原子操作】数据库事务
     * 只负责更新状态，速度极快
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateFileStatusInDb(ReportFiles reportFile) {
        reportFile.setIsDownLoad(1);
        saveToDataBase.updateReportFiles(reportFile);
    }

    // 辅助方法：安静删除文件
    private void deleteQuietly(File file) {
        if (file != null && file.exists()) {
            try {
                file.delete();
            } catch (Exception ignored) {

            }
        }
    }
}
