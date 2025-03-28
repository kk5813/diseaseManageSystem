package com.zcc.highmyopia;

import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeVO;
import com.zcc.highmyopia.AI.repository.DiagnoseRepository;
import com.zcc.highmyopia.AI.repository.IDiagnoseRepository;
import com.zcc.highmyopia.AI.service.tree.factory.DefaultTreeFactory;
import com.zcc.highmyopia.AI.service.tree.factory.engine.impl.DecisionTreeEngine;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.hospital.entity.VisitEntity;
import com.zcc.highmyopia.hospital.service.IDownLoadService;
import com.zcc.highmyopia.mapper.IUserMapper;
import com.zcc.highmyopia.po.Dept;
import com.zcc.highmyopia.po.Patients;
import com.zcc.highmyopia.po.User;
import com.zcc.highmyopia.po.Visits;
import com.zcc.highmyopia.service.IRedisService;
import com.zcc.highmyopia.service.IUserService;
import com.zcc.highmyopia.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SpringBootTest
class HighmyopiaApplicationTests {

    @Autowired(required = false)
    private IUserMapper userMapper;
    @Resource
    private IRedisService redissonService;

    @Test
    public void list() {
        List<User> userList = userMapper.list();
        userList.forEach(user -> {
            System.out.println(user);
        });
    }

    private long time = 1000 * 60 * 60 * 24;
    private String signature = "admin";

    @Test
    //JWT加密
    public void jwt() {
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwttoken = jwtBuilder
                //header
                .setHeaderParam("typ", "jwt")
                .setHeaderParam("alg", "HS256")
                //payload
                .claim("username", "tom")
                .claim("role", "admin")
                .setSubject("admin-test")
                .setExpiration(new Date(System.currentTimeMillis() + time))
                .setId(UUID.randomUUID().toString())
                //signature
                .signWith(SignatureAlgorithm.HS256, signature)
                .compact();      //用点拼接
        System.out.println(jwttoken);
    }

    @Test
    //jwt解密
    public void Parse() {
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNCIsImlhdCI6MTczMzM4NjU1OSwiZXhwIjoxNzMzOTkxMzU5fQ._XtxlrqSI8qA_7cDAG73CV5-oT55MsfctlSlZ5xWhihgNc72rybYmcLXsrAiSGREO2959Xcuc9StMkPIupWI8A";
        Claims claimByToken = jwtUtils.getClaimByToken(token);
        String subject = claimByToken.getSubject();
        System.out.println(subject);

    }

    //JwtUtils测试
    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void jwtUtils() {
        String jwtToken = jwtUtils.generateToken(1232153123L, 1);
        Claims claimByToken = jwtUtils.getClaimByToken(jwtToken);
        String subject = claimByToken.getSubject();
        System.out.println(subject);
        Integer status = (Integer) claimByToken.get("status");
        System.out.println(status);
    }



    @Resource
    private IRedisService redisService;
    //测试jedis
    @Test
    public void RedissionTest() {
        redissonService.setValue("zcc", "handsome");
        String value = redissonService.getValue("zcc");
        System.out.println(value);
    }


    //MD5测试
    @Autowired
    IUserService userService;
    @Test
    public void testSaltMD5Verify() {
        // 假设这是存储在数据库中的加密值
        String salt = "bEZ5mLq6SxkSkZHBnZxRfd8dyXGq7vfi"; // 存储的盐值
        String userPassword = "2287996531";  // 用户输入的密码
        //7ce7070696f2200bd57edb836265f9db
        // 对用户输入的密码和盐值进行加盐 MD5 加密
        String userPasswordHash = SecureUtil.md5(salt + SecureUtil.md5(userPassword));
        System.out.println(userPasswordHash);
    }



    @Test
    public void ShiroLogin(){
        // 1.初始化获取SecurityManager

        // 2.获取Subject对象

        // 3.创建token对象，wen应用用户密码

        // 4.完成登录
    }
    @Test
    void contextLoads() {

        //1. 获取连接
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.auth("2287996531");
        //2. 执行操作关闭连接
        jedis.set("name", "mannor");

        //3. 关闭连接
        jedis.close();
    }

    @Test
    void test_redisson(){
        redissonService.setValue("name", "zcc");
        String name = redissonService.getValue("name");
        System.out.println(name);
    }

    @Test
    void test_httpClientUtils() {
        String host = "http://10.0.225.6:8083";
        String path = "/api/report/getList";

        // 创建 RestTemplate 实例
        RestTemplate restTemplate = new RestTemplate();

        // 正确的目标 URL，URL 中使用占位符
        String url = host + path + "?physc_bdate={bdata}&physc_edate={edata}";

        // 发送 GET 请求，返回 ResponseEntity 对象
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, "20241112", "20241112");

        // 输出响应状态码
        System.out.println("Response Status Code: " + response.getStatusCodeValue());

        // 输出响应体
        System.out.println("Response Body: " + response.getBody());
    }

    @Test
    public void a() {
        // 使用 Date 类输出当前时间
        System.out.println(new Date());

        // 获取当前的 LocalDateTime
        LocalDateTime dateTime = LocalDateTime.now();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(simpleDateFormat.format(new Date()));  // 格式化当前日期

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(dateTimeFormatter.format(dateTime));  // 格式化 LocalDateTime
    }

    @Test
    public void test_entityToPo(){
        VisitEntity visitEntity = new VisitEntity();
        visitEntity.setPatientId(111L);
        visitEntity.setDoctorId(111L);
        visitEntity.setDeptId(111L);
        visitEntity.setSiteId(111L);
        visitEntity.setVisitNumber("123445566");
        Visits visits = VisitEntity.entityToPo(visitEntity);
        System.out.println(visits);
    }

    @Resource
    private IDownLoadService downLoadService;
    @Test
    public void getDataSet() throws InterruptedException {
        LocalDateTime today = LocalDateTime.of(2024,12,27,0,0);
        LocalDateTime now = LocalDateTime.of(2024,1,31,0,0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        while (now.isBefore(today)){
            String date = now.format(formatter);
            downLoadService.getFunds(date, date);
            now = now.plusDays(1);
            Thread.sleep(50);
        }
    }

    @Test
    public void redis_serve(){
        String cacheKey = Constants.RedisKey.PATIENTS + "1796037988798971906";
        Patients patients = redissonService.getValue(cacheKey);
        System.out.println(patients);
    }

    @Resource
    private DefaultTreeFactory defaultTreeFactory;
    @Resource
    private IDiagnoseRepository diagnoseRepository;
//    @Test
//    public void doDiagnose(){
//        RuleTreeVO ruleTreeVO = diagnoseRepository.getRuleTreeVOByDiseaseId(1);
//        DecisionTreeEngine decisionTreeEngine = defaultTreeFactory.openLogicTree(ruleTreeVO);
//        List<DiagnoseResultEntity> process = decisionTreeEngine.process(null);
//        System.out.println(process);
//
//    }


    @Test
    public void testMock(){
        String url = "http://127.0.0.1:4523/m1/3365319-628336-default/api/1"; // 请求的 URL

        // 创建 RestTemplate 实例
        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0"); // 模拟浏览器请求
        headers.set("Accept", "application/json"); // 请求返回 JSON 格式

        // 打印请求的 URL 和请求头
        System.out.println("Sending GET request to: " + url);
        System.out.println("Request Headers: " + headers);

        // 创建 HttpEntity，传入头部信息
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // 发送 GET 请求
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // 打印响应状态码和响应内容
            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
        } catch (Exception e) {
            // 打印异常信息
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void redisSet(){
        String cacheKey = String.valueOf(1);
        redissonService.setValue(cacheKey, "!");
    }

    @Test
    public void redisRemove(){
        String cacheKey = String.valueOf(1);
        redissonService.remove(cacheKey);
    }


}


