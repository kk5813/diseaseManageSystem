package com.zcc.highmyopia.addTestData;

import cn.hutool.crypto.SecureUtil;
import com.zcc.highmyopia.po.User;
import com.zcc.highmyopia.service.IUserService;
import com.zcc.highmyopia.util.SaltUtil;
import com.zcc.highmyopia.util.ShiroUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * @ClassName AddTestUser
 * @Description
 * @Author aigao
 * @Date 2024/12/23 15:38
 * @Version 1.0
 */
@Slf4j
@SpringBootTest
public class AddTestUser {
    @Resource
    IUserService userService;
    @Test
    public  void add() {
        User user = new User();
        user.setCreator("zcc");
        user.setCreateTime(LocalDateTime.now());
        String salt = SaltUtil.getSalt();
        String password = SecureUtil.md5(salt + SecureUtil.md5("hos9344"));
        user.setUserPassword(password);
        user.setSalt(salt);
        user.setUserLoginName("admins1");
        user.setUserName("管理员1");
        user.setUserStatus(0);
        log.info("用户注册 user:{}", user);
        userService.saveOrUpdate(user);
    }
    // 生成1000个用户
    private static final int TOTAL_USERS = 1;

    // 存储已存在的UserLoginName
    private static final Set<String> existingLoginNames = new HashSet<>();

    // 存储用户原始密码的文件
    private static final String OUTPUT_FILE = "original_passwords.csv";
    /**
     * @deleteSql DELETE FROM user WHERE creator = 'aigao' AND user_status = 1;
     * @Desciption 这里批量导入用户，删除用上面的sql语句数据库直接删，密码在文件original_passwords.csv里面
     * */
    @Test
    public void addMuchUser() throws IOException {
        // 创建文件并准备写入
        BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE));

        // 生成1000个用户
        for (int i = 0; i < TOTAL_USERS; i++) {
            // 随机生成用户名和密码
            String userLoginName = generateUniqueLoginName();
            String password = generatePassword();

            // 构建User对象
            User user = new User();
            user.setCreator("aigao");
            user.setCreateTime(LocalDateTime.now());
            String salt = SaltUtil.getSalt();
            String encryptedPassword = SecureUtil.md5(salt + SecureUtil.md5(password));
            user.setUserPassword(encryptedPassword);
            user.setSalt(salt);
            user.setUserLoginName(userLoginName);
            user.setUserName(userLoginName);  // 用户名和登录名相同
            user.setUserStatus(1);

            // 输出日志
            log.info("用户注册 user:{}", user);
            userService.saveOrUpdate(user);
            // 写入原始密码到CSV文件
            writer.write(userLoginName + "," + password + "\n");
        }

        // 关闭文件写入
        writer.close();
    }

    // 生成唯一的UserLoginName
    private String generateUniqueLoginName() {
        String loginName;
        do {
            loginName = "user" + new Random().nextInt(1000000);  // 生成类似user123456的登录名
        } while (existingLoginNames.contains(loginName));
        existingLoginNames.add(loginName);
        return loginName;
    }

    // 随机生成密码
    private String generatePassword() {
        int length = 8 + new Random().nextInt(8); // 密码长度8到15个字符
        StringBuilder password = new StringBuilder();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(new Random().nextInt(chars.length())));
        }
        return password.toString();
    }


}
