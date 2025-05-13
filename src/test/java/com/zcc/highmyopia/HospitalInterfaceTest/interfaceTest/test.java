package com.zcc.highmyopia.HospitalInterfaceTest.interfaceTest;

import org.junit.Test;

import java.time.LocalDateTime;
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
    String host="http://acloud.aierchina.com:8010/external-api";
    //String host = "http://sit.aierchina.com:8710/external-api";
    String hospId = "1824";
    String appKey = "Cdm_1824";
    //String appSecret="0ihd.e4zc";
    String appSecret="h2md.scxm";

    /**
     * 请求host: http://sit.aierchina.com:8710/external-api
     * 请求path：/api/interface/medical/getPatientVisit
     * 请求方式：POST
     * 接口描述：该接口用于查询患者基本信息和就诊信息
     *  @Description : 请求参数，返回参数名和类型，均无问题
     *  @available : True
     *  @Date : 2024-03-11 ok
     *  @tester :aigao
     */
    @Test
    public  void testPatientVisit() throws Exception {
        //String host = "http://sit.aierchina.com:8710/external-api";
        String path = "/api/interface/medical/getPatientVisit";
        int connectTimeout=7200;
//        String hospId ="1824";
//        String appKey = "cdm_1824";
////        String appSecret="ywgp.lbol";
////        String appKey = "Cdm";
//        String appSecret="0ihd.e4zc";
//        //String appSecret = "0ihd.e4zc";
        Map<String,String> querys = new HashMap<>();
//        querys.put("diagBdate","20241120");
//        querys.put("diagEdate","20241121");
        querys.put("diagBdate","20250316");
        querys.put("diagEdate","20250317");
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
     * 请求path：/api/interface/medical/getOutpRecipe
     * 请求方式：POST
     * 接口描述：该接口用于查询患者处方信息
     *  @Description : 请求参数，返回参数名和类型，均无问题
     *  @available : True
     *  @Date : 2024-03-11 ok
     *  @tester :aigao
     */
    @Test
    public  void testGetRecope() throws Exception {
        //String host="http://sit.aierchina.com:8710/external-api";
        String path = "/api/interface/medical/getOutpRecipe";
        int connectTimeout=7200;
//        String hospId ="1824";
//        String appKey = "Cdm_1824";
//        String appSecret = "0ihd.e4zc";
////        String appSecret="ywgp.lbol";

        Map<String,String> querys = new HashMap<>();
//        querys.put("billingBdate","20241126");
//        querys.put("billingEdate","20241127");
        querys.put("billingBdate","20250306");
        querys.put("billingEdate","20250307");
        String reqJson ="";
        Map<String,String>headers =new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpPostJson(host, path, connectTimeout, headers, querys, reqJson,signHeaderPrefixList,appKey,appSecret,hospId);System.out.println("结果:"+resp.getBody());
        System.out.println(resp.getContentType());
        System.out.println(resp.getErrorMessage());
        System.out.println(resp.getStatusCode());
    }


    /**
     *请求host: http://sit.aierchina.com:8710/external-api
     *请求path：/alis/interface/reportDetail/getReportDetail
     *请求方式：POST
     *接口描述：该接口用于查询患者的检验结果
     * @Description : 请求参数，返回参数名和类型，均无问题
     * @specail : 这个接口可以不传 visitNumber
     * @available : True
     * @Date : 2024-03-11 ok
     * @tester :aigao
     */
    @Test
    public  void testGetReportDetail() throws Exception {
        //String host="http://sit.aierchina.com:8710/external-api";
//        String host="http://sit.aierchina.com:8707";
        String path = "/alis/interface/reportDetail/getReportDetail";
        int connectTimeout = 7200;
//        String hospId = "1824";
//        String appKey = "Cdm_1824";
////        String appSecret = "ywgp.lbol";
////        String appKey = "cdm_1824";
//        String appSecret="0ihd.e4zc";
        Map<String, String> r = new HashMap<>();
//        r.put("auditDateBegin", "2024-11-13");
//        r.put("auditDateEnd", "2024-11-15");
        r.put("auditDateBegin","2025-03-06");
        r.put("auditDateEnd","2025-03-07");
//        r.put("visitNumber", "1134234324");
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
        //String host = "http://sit.aierchina.com:8707";
//        String host = "http://sit.aierchina.com:8710/external-api";
        String path = "/api/aemro/outpElement/getOutpElementByCondition";
        int connectTimeout=7200;
//        String hospId = "1824";
////        String appKey = "Cdm_1824";
//        String appSecret = "ywgp.lbol";
//        //String appSecret = "0ihd.e4zc";
//        String appKey = "cdm_1824";
////        String appSecret=" 0ihd.e4zc";


        Map<String,String> querys = new HashMap<>();
//        querys.put("aemr_bdate","2024-06-26");
//        querys.put("aemr_edate","20240725");
        querys.put("aemr_bdate","2025-08-01");
        querys.put("aemr_edate","2025-08-15");
        querys.put("Visit_number","MZ202408150129");
//        querys.put("Visit_number","MZ202407071064");
        String reqJson = JsonUtil.toJson(querys);
        Map<String,String>headers =new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpPostJson(host, path, connectTimeout, headers, querys, reqJson,signHeaderPrefixList,appKey,appSecret,hospId);
        System.out.println(resp.getBody());
        System.out.println(resp.getStatusCode());
    }


    /**
    * 请求host: http://sit.aierchina.com:8710/external-api
     * 请求path：/api/interface/patientInfo/getById
     * 请求方式：POST
     * 接口描述：该接口用于查询患者敏感信息（电话）
     *  @Description : 请求参数，返回参数名无问题，参数返回类型中sex为string而非int
     *  @available : True | error
     *  @Date : 2024-03-11 ok
     *  @tester :aigao
    */
    @Test
    public  void testGetInfo() throws Exception {
        //String host="http://sit.aierchina.com:8710/external-api";
        String path = "/api/interface/patientInfo/getById";
        int connectTimeout=7200;
//        String hospId ="1824";
//        String appKey = "cdm_1824";
//        String appSecret="ywgp.lbol";
////        String appKey = "cdm_1824";
////        String appSecret=" 0ihd.e4zc";

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
     * 请求host: http://sit.aierchina.com:8710
     * 请求path：/external-api/avis/interface/deviceDocking/getAutoVisionByVisitNumber
     * 请求方式：get
     * 接口描述：该接口用于慢病管理体察数据查询接口
     *  @Description :
     *  @available : True
     *  @Date : 2024-12-24
     *  @tester :aigao
     */
    @Test
    public  void testgetAutoVisionByVisitNumber() throws Exception {
       // String host = "http://sit.aierchina.com:8710/external-api";
        String path = "/avis/interface/deviceDocking/getAutoVisionByVisitNumber";
        int connectTimeout=7200;
//        String HospId = "9999";
//        String appKey = "aviseq_9999";
//        String appSecret = "lsj.z4HzPyAA";

        Map<String,String> querys = new HashMap<>();
//        querys.put("checkBdate","2024-06-29");
//        querys.put("checkEdate","2024-07-08");
        querys.put("checkBdate","2025-01-20");
        querys.put("checkEdate","2025-01-20");
        // querys.put("visitNumber","MZ202407070797");
        querys.put("visitNumber","MZ202501200351");
        String reqJson = JsonUtil.toJson(querys);
        Map<String,String>headers =new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpGet(host, path, connectTimeout, headers, querys, signHeaderPrefixList,appKey,appSecret,hospId);
        System.out.println(resp.getBody());
        System.out.println(resp.getStatusCode());
    }
}