package com.zcc.highmyopia.HospitalInterfaceTest.interfaceTest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName test
 * @Description
 * @Author aigao
 * @Date 2024/11/15 16:38
 * @Version 1.0
 */
public class test {
    HashMap<String,String> Maps = new HashMap<>();
    /**
     *请求host: http://sit.aierchina.com:8710/external-api
     *请求path：/alis/interface/reportDetail/getReportDetail
     *请求方式：POST
     *接口描述：该接口用于查询患者的检验结果
     * @Description : 请求参数，返回参数名和类型，均无问题
     * @specail : 这个接口可以不传 visitNumber
     * @available : True
     * @Date : 2024-12-18
     * @tester :aigao
     */
    @Test
    public  void testGetReportDetail() throws Exception {
        String host="http://sit.aierchina.com:8710/external-api";
        String path = "/alis/interface/reportDetail/getReportDetail";
        int connectTimeout = 7200;
        String hospId = "1824";
        String appKey = "Cdm_1824";
        String appSecret = "ywgp.lbol";

        Map<String, String> r = new HashMap<>();
        r.put("auditDateBegin", "2024-08-01");
        r.put("auditDateEnd", "2024-11-15");
        //r.put("visitNumber", "1134234324");
        String reqJson = JsonUtil.toJson(r);

        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> querys = null;
        List<String> signHeaderPrefixList = new ArrayList<>();

        Response resp = HttpClientUtils.httpPostJson(host, path, connectTimeout, headers, querys, reqJson,
                signHeaderPrefixList, appKey, appSecret, hospId);
        System.out.println("结果:" + resp.getBody());
        System.out.println(resp.getStatusCode());
    }

    /**
     * 请求host: http://sit.aierchina.com:8710/external-api
     * 请求path：/api/interface/medical/getOutpRecipe
     * 请求方式：POST
     * 接口描述：该接口用于查询患者处方信息
     *  @Description : 请求参数，返回参数名和类型，均无问题
     *  @available : True
     *  @Date : 2024-12-18
     *  @tester :aigao
     */
    @Test
    public  void testGetRecope() throws Exception {
        String host="http://sit.aierchina.com:8710/external-api";
        String path = "/api/interface/medical/getOutpRecipe";
        int connectTimeout=7200;
        String hospId ="1824";
        String appKey = "Cdm_1824";
        //String appSecret = "0ihd.e4zc";
        String appSecret="ywgp.lbol";

        Map<String,String> querys = new HashMap<>();
        querys.put("billingBdate","20240801");
        querys.put("billingEdate","20241125");
        String reqJson ="";
        Map<String,String>headers =new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpPostJson(host, path, connectTimeout, headers, querys, reqJson,signHeaderPrefixList,appKey,appSecret,hospId);System.out.println("结果:"+resp.getBody());
        System.out.println(resp.getContentType());
        System.out.println(resp.getErrorMessage());
        System.out.println(resp.getStatusCode());
    }
    /**
     * 请求host: http://sit.aierchina.com:8710/external-api
     * 请求path：/api/interface/medical/getPatientVisit
     * 请求方式：POST
     * 接口描述：该接口用于查询患者基本信息和就诊信息
     *  @Description : 请求参数，返回参数名和类型，均无问题
     *  @available : True
     *  @Date : 2024-12-18
     *  @tester :aigao
     */
    @Test
    public  void testPatientVisit() throws Exception {
        String host = "http://sit.aierchina.com:8710/external-api";
        String path = "/api/interface/medical/getPatientVisit";
        int connectTimeout=7200;
        String hospId ="1824";
        String appKey = "Cdm_1824";
        //String appSecret="ywgp.lbol";
        String appSecret = "0ihd.e4zc";
        Map<String,String> querys = new HashMap<>();
        querys.put("diagBdate","20240520");
        querys.put("diagEdate","20240529");
        String reqJson ="";
        Map<String,String>headers =new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();

        Response resp = HttpClientUtils.httpPostJson(host, path, connectTimeout, headers, querys, reqJson,
                signHeaderPrefixList, appKey, appSecret, hospId);
        System.out.println("结果:" + resp.getBody());
        System.out.println(resp.getStatusCode());
    }


    /**
    * 请求host: http://sit.aierchina.com:8710/external-api
     * 请求path：/api/interface/patientInfo/getById
     * 请求方式：POST
     * 接口描述：该接口用于查询患者敏感信息（电话）
     *  @Description : 请求参数，返回参数名无问题，参数返回类型中sex为string而非int
     *  @available : True | error
     *  @Date : 2024-12-18
     *  @tester :aigao
    */
    @Test
    public  void testGetInfo() throws Exception {
        String host="http://sit.aierchina.com:8710/external-api";
        String path = "/api/interface/patientInfo/getById";
        int connectTimeout=7200;
        String hospId ="1824";
        String appKey = "Cdm_1824";
        String appSecret="ywgp.lbol";

        Map<String, String> r = new HashMap<>();
        r.put("id", "1796786711460069377");
        String reqJson = JsonUtil.toJson(r);

        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> querys = null;
        List<String> signHeaderPrefixList = new ArrayList<>();

        Response resp = HttpClientUtils.httpPostJson(host, path, connectTimeout, headers, querys, reqJson,
                signHeaderPrefixList, appKey, appSecret, hospId);
        System.out.println("结果:" + resp.getBody());
        System.out.println(resp.getStatusCode());
    }
    /**
     * 请求host: http://sit.aierchina.com:8707
     * 请求path：/api/aemro/outpElement/getOutpElementByCondition
     * 请求方式：POST
     * 接口描述：该接口用于查询患者门诊病历
     *  @Description : 请求参数，返回参数名和类型，均无问题
     *  @available : True
     *  @Date : 2024-12-18
     *  @tester :aigao
     */
    @Test
    public  void testGetgetOutElementByCondition() throws Exception {
        String host = "http://sit.aierchina.com:8707";
        String path = "/api/aemro/outpElement/getOutpElementByCondition";
        int connectTimeout=7200;
        String hospId = "1824";
        String appKey = "Cdm_1824";
        String appSecret = "ywgp.lbol";

        Map<String,String> querys = new HashMap<>();
        querys.put("aemr_bdate","2024-06-26");
        querys.put("aemr_edate","20240725");
        querys.put("Visit_number","MZ202407071064");
        String reqJson = JsonUtil.toJson(querys);
        Map<String,String>headers =new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpPostJson(host, path, connectTimeout, headers, querys, reqJson,signHeaderPrefixList,appKey,appSecret,hospId);
        System.out.println(resp.getBody());
        System.out.println(resp.getStatusCode());
    }
}