package com.zcc.highmyopia.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.entity.User;
import com.zcc.highmyopia.service.UserService;
import com.zcc.highmyopia.shiro.AccountProfile;
import com.zcc.highmyopia.util.JwtUtils;
import com.zcc.highmyopia.util.SaltUtil;
import com.zcc.highmyopia.util.ShiroUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/${app.config.api-version}/user")
public class UserController {


    private final UserService userService;

    private final JwtUtils jwtUtils;

    // 测试用
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

    // 添加用户
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
        log.info("用户注册 user:{}", temp);
        userService.saveOrUpdate(temp);
        return Result.succ(null);
    }

    // 编辑用户
    @PostMapping("/edit")
    @RequiresAuthentication
    public Result editUser(@RequestBody User user, HttpServletRequest request) {
        // 身份校验:只有管理员才有权限修改
        User temp = userService.getById(user.getUserId());
        String jwtToken = request.getHeader("Authorization");
        Claims claimByToken = jwtUtils.getClaimByToken(jwtToken);

        Integer status = (Integer) claimByToken.get("status");
        log.info("当前用户的权限是：{}", status);
        if (status != 0)
            return Result.fail(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getInfo(),null);

        temp.setModifier(ShiroUtil.getProfile().getUserName());
        temp.setUpdateTime(LocalDateTime.now());
        temp.setUserLoginName(user.getUserLoginName());
        temp.setUserName(user.getUserName());
        temp.setUserStatus(user.getUserStatus());
        temp.setUserPassword(user.getUserPassword());

        userService.saveOrUpdate(temp);
        return Result.succ(null);
    }

    // 测试保存
    @PostMapping("/save")
    @RequiresAuthentication
    public Object testUser(@Validated @RequestBody User user) {
        return user.toString();
    }

    // 失效某用户
    @GetMapping("/invalid/{userId}")
    @RequiresAuthentication
    public Result invalidUser(@PathVariable(name = "userId") Long userId, HttpServletRequest request) {
        // 身份校验:只有管理员才有权限修改
        String jwtToken = request.getHeader("Authorization");
        Claims claimByToken = jwtUtils.getClaimByToken(jwtToken);

        Integer status = (Integer) claimByToken.get("status");
        log.info("当前用户的权限是：{}", status);
        if (status != 0)
            return Result.fail(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getInfo(),null);

        User temp = userService.getById(userId);
        temp.setUserStatus(-1);
        userService.saveOrUpdate(temp);
        return Result.succ(null);
    }

    // 查找
    @GetMapping("/find/{userId}")
    @RequiresAuthentication
    public Result FindUser(@PathVariable(name = "userId") Long userId) {
        User temp = userService.getById(userId);
        log.info("用户" + temp);
        return Result.succ(temp);
    }

    // 用户列表
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

    @GetMapping("/listPage")
    public Result getUsersPage(@RequestParam(defaultValue = "0") int page,  // 页码默认 0
                               @RequestParam(defaultValue = "10") int size) {  // 每页大小默认 10
        return userService.getUsersPage(page, size);
    }
}