package com.zcc.highmyopia.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.entity.User;
import com.zcc.highmyopia.service.UserService;
import com.zcc.highmyopia.shiro.AccountProfile;
import com.zcc.highmyopia.util.SaltUtil;
import com.zcc.highmyopia.util.ShiroUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liangyue
 * @since 2021-02-01
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    //          测试用
    @GetMapping("/index")
    public Object index() {
        User user = userService.getById(1L);
        return Result.succ(MapUtil.builder()
                .put("userId", user.getUserId())
                .put("userLoginName", user.getUserLoginName())
                .put("userName", user.getUserName())
                .map()
        );
    }


    //          测试保存
    @PostMapping("/save")
    @RequiresAuthentication
    public Object testUser(@Validated @RequestBody User user) {
        return user.toString();
    }


    //          添加用户
    @PostMapping("/add")
    @RequiresAuthentication
    public Result addUser(@Validated @RequestBody User user) {
        User temp = new User();
        temp.setCreator(ShiroUtil.getProfile().getUserName());
        temp.setCreateTime(LocalDateTime.now());
        String salt = SaltUtil.getSalt();
        String password = SecureUtil.md5(salt + SecureUtil.md5(user.getUserPassword()));
        temp.setUserPassword(password);
        temp.setSalt(salt);
        temp.setUserLoginName(user.getUserLoginName());
        temp.setUserName(user.getUserName());
        temp.setUserStatus(user.getUserStatus());
        userService.saveOrUpdate(temp);
        return Result.succ(null);
    }


    //          编辑用户
    @PostMapping("/edit")
    @RequiresAuthentication
    public Result editUser( @RequestBody User user) {
        User temp = userService.getById(user.getUserId());

        temp.setModifier(ShiroUtil.getProfile().getUserName());
        temp.setUpdateTime(LocalDateTime.now());
        temp.setUserLoginName(user.getUserLoginName());
        temp.setUserName(user.getUserName());
        temp.setUserStatus(user.getUserStatus());
        temp.setUserPassword(user.getUserPassword());

        userService.saveOrUpdate(temp);
        return Result.succ(null);
    }

    //          失效某用户
    @GetMapping("/invalid/{userId}")
    @RequiresAuthentication
    public Result invalidUser(@PathVariable(name = "userId") Long userId) {
        User temp = userService.getById(userId);
//        System.out.println("==============");
//        System.out.println(temp);
        temp.setUserStatus(-1);
        userService.saveOrUpdate(temp);
        return Result.succ(null);
    }

    //    查找
    @GetMapping("/find/{userId}")
    @RequiresAuthentication
    public Result FindUser(@PathVariable(name = "userId") Long userId) {
        User temp = userService.getById(userId);
        log.info("用户" + temp);
        return Result.succ(temp);
    }

    //          用户列表
    @GetMapping("/list")
    @RequiresAuthentication
    public Result list() {
        log.info("userController的list方法");
        System.out.println("userController的list方法");
        List<User> userList = userService.list();
        List<AccountProfile> resList = new ArrayList<>();
        for(User user : userList) {
            AccountProfile profile = new AccountProfile();
            BeanUtil.copyProperties(user, profile);
            resList.add(profile);
        }
        log.info("查询成功" + userList);
        System.out.println("查询成功" + userList);
        return Result.succ(resList);
    }
}