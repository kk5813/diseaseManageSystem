package com.zcc.highmyopia.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.common.vo.UserVO;
import com.zcc.highmyopia.po.User;
import com.zcc.highmyopia.service.IUserService;
import com.zcc.highmyopia.shiro.AccountProfile;
import com.zcc.highmyopia.util.JwtUtils;
import com.zcc.highmyopia.util.SaltUtil;
import com.zcc.highmyopia.util.ShiroUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
 * @Author zcc
 * @Date 2024/12/18
 * @Description
 */
@Slf4j
@RequiredArgsConstructor
@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/${app.config.api-version}/user")
public class UserController {


    private final IUserService userService;

    private final JwtUtils jwtUtils;

    @PostMapping("/add")
    @ApiOperation(value = "用户注册")
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
        temp.setUserStatus(1);
        log.info("用户注册 user:{}", temp);
        userService.saveOrUpdate(temp);
        return Result.succ(null);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑用户")
    @RequiresAuthentication
    public Result editUser(@RequestBody User user, HttpServletRequest request) {
        String jwtToken = request.getHeader("Authorization");
        Claims claimByToken = jwtUtils.getClaimByToken(jwtToken);
        Integer status = (Integer) claimByToken.get("status");
        log.info("当前用户的权限是：{}", status);
        String userId = claimByToken.getSubject();
        log.info("当前用户的ID是：{}", userId);
        // 身份校验:只有管理员才有权限修改

        if (status != 0)
            return Result.fail(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getInfo(),null);
        User temp = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserLoginName, user.getUserLoginName()));
        temp.setModifier(ShiroUtil.getProfile().getUserName());
        temp.setUpdateTime(LocalDateTime.now());
        temp.setUserLoginName(user.getUserLoginName());
        temp.setUserName(user.getUserName());
        temp.setUserStatus(user.getUserStatus());
        temp.setUserPassword(SecureUtil.md5(user.getSalt() + SecureUtil.md5(user.getUserPassword())));

        userService.saveOrUpdate(temp);
        return Result.succ(null);
    }

    @GetMapping("/invalid/{userId}")
    @ApiOperation(value = "失效某用户")
    @RequiresAuthentication
    public Result invalidUser(@PathVariable(name = "userId") Long userId, HttpServletRequest request) {
        // 身份校验:只有管理员才有权限修改
        String jwtToken = request.getHeader("Authorization");
        Claims claimByToken = jwtUtils.getClaimByToken(jwtToken);

        Integer status = (Integer) claimByToken.get("status");
        log.info("当前用户的权限是：{}", status);
        if (status != 0) {
            return Result.fail(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getInfo(),null);
        }

        User temp = userService.getById(userId);
        temp.setUserStatus(-1);
        userService.saveOrUpdate(temp);
        return Result.succ(null);
    }

    @GetMapping("/find/{userId}")
    @ApiOperation(value = "精确查找用户")
    @RequiresAuthentication
    public Result FindUser(@PathVariable(name = "userId") Long userId) {
        User user = userService.getById(userId);
        return Result.succ(user);
    }
    @GetMapping("/search")
    @ApiOperation(value = "模糊查找用户")
    @RequiresAuthentication
    public Result SearchUser(@RequestBody User user) {
        List<User> users = userService.SearchUser(user);
        Long total = (long) users.size();
        UserVO userVO = new UserVO();
        userVO.setUsers(users);
        userVO.setTotal(total);
        return Result.succ(userVO);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询用户")
    @RequiresAuthentication
    public Result getUsersPage(@RequestParam(defaultValue = "1") int pageNumber,  // 页码默认 0
                               @RequestParam(defaultValue = "10") int pageSize) {  // 每页大小默认 10
        return userService.getUsersPage(pageNumber, pageSize);
    }
}