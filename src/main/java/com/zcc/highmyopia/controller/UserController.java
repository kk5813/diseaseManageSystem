package com.zcc.highmyopia.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.po.User;
import com.zcc.highmyopia.service.IUserService;
import com.zcc.highmyopia.shiro.AccountProfile;
import com.zcc.highmyopia.util.JwtUtils;
import com.zcc.highmyopia.util.SaltUtil;
import com.zcc.highmyopia.util.ShiroUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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


    private final IUserService userService;

    private final JwtUtils jwtUtils;

    // 用户注册
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
        User user = userService.getById(userId);
        return Result.succ(user);
    }

    // 分页查询
    @GetMapping("/listPage")
    @RequiresAuthentication
    public Result getUsersPage(@RequestParam(defaultValue = "0") int page,  // 页码默认 0
                               @RequestParam(defaultValue = "10") int size) {  // 每页大小默认 10
        return userService.getUsersPage(page, size);
    }
}