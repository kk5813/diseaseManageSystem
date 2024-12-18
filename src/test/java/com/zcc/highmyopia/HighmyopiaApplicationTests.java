package com.zcc.highmyopia;

import cn.hutool.crypto.SecureUtil;
import com.zcc.highmyopia.po.User;
import com.zcc.highmyopia.mapper.IUserMapper;
import com.zcc.highmyopia.service.IRedisService;
import com.zcc.highmyopia.service.IUserService;
import com.zcc.highmyopia.util.JwtUtils;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
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



    //测试jedis
    @Test
    public void JedisTest() {
        //redis的java客戶端jedis
        //建立连接
        Jedis jedis = new Jedis("localhost", 6379);
        //设置密码
        jedis.auth("2287996531");
        //选择库,不选择默认为0
        jedis.select(0);
        jedis.set("key1", "string");
        jedis.del("key1");
        //释放资源
        jedis.close();
    }


    //MD5测试
    @Autowired
    IUserService userService;
    @Test
    public void testSaltMD5() {
        String userPassword = "2287996531";    //明文密码
        String loginDto = SecureUtil.md5(userPassword);   //使用MD5加密
        //Md5Hash md5Hash = new Md5Hash(userPassword);
        String salt = "bEZ5mLq6SxkSkZHBnZxRfd8dyXGq7vfi";

        System.out.println(SecureUtil.md5(salt + loginDto));
        //System.out.println(SecureUtil.md5(md5Hash + salt));
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

}
