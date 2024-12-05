package com.zcc.highmyopia;

import cn.hutool.crypto.SecureUtil;
import com.zcc.highmyopia.entity.User;
import com.zcc.highmyopia.mapper.UserMapper;
import com.zcc.highmyopia.service.IRedisService;
import com.zcc.highmyopia.service.UserService;
import com.zcc.highmyopia.service.impl.RedissonService;
import com.zcc.highmyopia.util.JwtUtils;
import io.jsonwebtoken.*;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
class HighmyopiaApplicationTests {

    @Autowired(required = false)
    private UserMapper userMapper;
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
        String token = "eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRvbSIsInJvbGUiOiJhZG1pbiIsInN1YiI6ImFkbWluLXRlc3QiLCJleHAiOjE2OTkxNjcxNTQsImp0aSI6Ijg2YzYyYjQ4LTQ4MTUtNDFlZC05NDI2LWJlYWUxYTdiNzU1YSJ9.UxSvnZjmKNOIakO8C2ETCyqmLKedV9_rI6LNi_3HAvk";
        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        System.out.println(claims.get("username"));
        System.out.println(claims.get("role"));
        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getExpiration());
    }

    //JwtUtils测试
    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void jwtUtils() {
        String jwtToken = jwtUtils.generateToken(1232153123L);
        Claims claims = jwtUtils.getClaimByToken(jwtToken);
        System.out.println(claims.getExpiration());
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
    UserService userService;
    @Test
    public void testSaltMD5() {
        String userPassword = "2287996531";    //明文密码
        String loginDto = SecureUtil.md5(userPassword);   //使用MD5加密
        Md5Hash md5Hash = new Md5Hash(userPassword);
        String salt = "bEZ5mLq6SxkSkZHBnZxRfd8dyXGq7vfi";

        System.out.println(SecureUtil.md5(salt + loginDto));
        System.out.println(SecureUtil.md5(md5Hash + salt));
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

}
