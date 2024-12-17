package com.zcc.highmyopia.hospital.service;

import com.alibaba.fastjson.JSON;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.hospital.entity.*;
import com.zcc.highmyopia.hospital.repository.ISaveRepository;
import com.zcc.highmyopia.hospital.utils.HttpClientUtils;
import com.zcc.highmyopia.hospital.utils.Response;
import com.zcc.highmyopia.mapper.IReportFilesMapper;
import com.zcc.highmyopia.po.ReportFiles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oracle.security.o5logon.a;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

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
    @Value("${hospital.AHisHost}")
    private String AHisHost;
    @Value("${hospital.APacsHost}")
    private String APacsHost;
    @Value("${hospital.filePath}")
    private String targetPath;

    private final int connectTimeOut = 7200;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ISaveRepository saveRepository;

    // 患者就诊信息
    @Override
    public List<VisitEntity> getPatientVisit(String beginData, String endData) throws Exception {
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
        if (statusCode != 200)
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        String body = resp.getBody();
        List<VisitEntity> visitEntities = JSON.parseArray(body, VisitEntity.class);
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
        List<RecipeEntity> recipeEntities = JSON.parseArray(body, RecipeEntity.class);

         saveRepository.saveRecipeAndOrderDetail(recipeEntities);
    }

    // 检查结果 CheckResult
    @Override
    public void getReportDetail(String beginData, String endData, String visitNumber) throws Exception {
        String path = "/alis/interface/ReportDetail/getReportDetail";
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
        CheckResultsEntity checkResultEntity = JSON.parseObject(body, CheckResultsEntity.class);
        saveRepository.saveCheckResult(checkResultEntity);
    }

    @Override
    public void getReportDetail(String beginData, String endData, List<String> visitNumbers) throws Exception {
        visitNumbers.forEach(visitNumber -> {
            try {
                getReportDetail(beginData, endData, visitNumber);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void getOutElementByCondition(String beginData, String endData, String visitNumber) throws Exception {
        String path = "/api/aemro/outpElement/getOutpElementByCondition";

        Map<String, String> query = new HashMap<>();
        query.put("aemrBdate", beginData);
        query.put("aemrEdate", endData);
        query.put("Visit_number",visitNumber);
        String reqJson = "";
        Map<String, String> headers = new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpPostJson(AHisHost, path, connectTimeOut, headers,
                query, reqJson, signHeaderPrefixList, appKey, appSecret, hospId);
        int statusCode = resp.getStatusCode();
        if (statusCode != 200)
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        String body = resp.getBody();
        ElementEntity elementEntity = JSON.parseObject(body, ElementEntity.class);
        saveRepository.saveElement(elementEntity);
    }

    @Override
    public void getOutElementByCondition(String beginData, String endData, List<String> visitNumbers) throws Exception {
        visitNumbers.forEach(visitNumber -> {
            try {
                getOutElementByCondition(beginData, endData, visitNumber);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void getCheckResult(String beginData, String endData, List<String> visitNumbers) {
        visitNumbers.forEach(visitNumber -> {
            try {
                getCheckResult(beginData, endData, visitNumber);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void getCheckResult(String beginData, String endData, String visitNumber) {
        if (visitNumber == null || visitNumber.isEmpty())
            throw new AppException(500, "就诊号字段为必填字段");
        String path = "/api/report/getList";
        String param = "beginData=" + beginData + "&"
                + "endData=" + endData + "&"
                + "visitNumber=" + visitNumber ;
        String url = APacsHost + path + "?{param}";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, param);

        int statusCode = response.getStatusCodeValue();
        if (statusCode != 200)
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        String body = response.getBody();
        CheckReportsEntity checkReportsEntity = JSON.parseObject(body, CheckReportsEntity.class);
        saveRepository.saveCheckReportsAndReportFiles(checkReportsEntity);
    }



    @Override
    public void DownLoadReportImage(ReportFiles reportFile) {

        String urls = APacsHost + reportFile.getFileUrl();  // 获取文件的URL

        ResponseEntity<byte[]> response = restTemplate.getForEntity(urls, byte[].class);
        int statusCode = response.getStatusCodeValue();

        // 判断HTTP状态码是否为200
        if (statusCode != 200) {
            throw new AppException(statusCode, "请求获取患者就诊信息失败");
        }

        byte[] fileBytes = response.getBody();  // 获取文件内容的字节数组

        String fileType = reportFile.getFileType();
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
            // 更新状态为已下载
            reportFile.setIsDownLoad((short) 1);  // 标记文件已下载
            reportFile.setFilePath(targetFile.getAbsolutePath());  // 设置文件路径
            saveRepository.updateReportFiles(reportFile);  // 更新数据库
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件保存失败", e);
        }
    }


    @Override
    public void DownLoadReportImageBatch() {
        List<ReportFiles> reportFiles = saveRepository.DownLoadReportImageBatch();
        reportFiles.forEach(this::DownLoadReportImage);
    }
}
