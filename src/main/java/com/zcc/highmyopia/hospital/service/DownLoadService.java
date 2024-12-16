package com.zcc.highmyopia.hospital.service;

import com.alibaba.fastjson.JSON;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.hospital.entity.VisitEntity;
import com.zcc.highmyopia.hospital.utils.HttpClientUtils;
import com.zcc.highmyopia.hospital.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author zcc
 * @Date 2024/12/16
 * @Description 第三方平台获取数据的接口
 */
@Slf4j
@Service
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
    private final int connectTimeOut = 7200;
    private final RestTemplate restTemplate = new RestTemplate();

    // 患者就诊信息
    @Override
    public void getPatientVisit(String beginData, String endData) throws Exception {
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
        VisitEntity visitEntity = JSON.parseObject(body, VisitEntity.class);

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
        System.out.println(resp.getBody());
        System.out.println(resp.getStatusCode());
    }

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
        System.out.println(resp.getStatusCode());
        System.out.println(resp.getBody());
    }

    @Override
    public void getOutElementByCondition(String beginData, String endData) throws Exception {
        String path = "/api/aemro/outpElement/getOutpElementByCondition";

        Map<String, String> query = new HashMap<>();
        query.put("aemrBdate", beginData);
        query.put("aemrEdate", endData);
        String reqJson = "";
        Map<String, String> headers = new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpPostJson(AHisHost, path, connectTimeOut, headers,
                query, reqJson, signHeaderPrefixList, appKey, appSecret, hospId);
        System.out.println(resp.getBody());
        System.out.println(resp.getStatusCode());
    }

    @Override
    public void getCheckResult(String beginData, String endData, String patientId) {
        String path = "/api/report/getList";
        String param = "beginData=" + beginData + "&"
                + "endData=" + endData;
        if (patientId != null)
            param = param + "&" + "patientId=" + patientId;
        String url = APacsHost + path + "?{param}";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, param);

        System.out.println("Response Status Code: " + response.getStatusCodeValue());
        System.out.println("Response Body: " + response.getBody());
    }

    @Override
    public void getReportImage(String url) {
        String urls = APacsHost + url;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        System.out.println("Response Status Code: " + response.getStatusCodeValue());
        System.out.println("Response Body: " + response.getBody());
    }
}
