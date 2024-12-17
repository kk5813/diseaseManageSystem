package com.zcc.highmyopia.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.po.Doctor;
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
@RequestMapping("/api/${app.config.api-version}/doctor")
public class DoctorController {


    // 添加用户
    @PostMapping("/add")
    @RequiresAuthentication
    public Result addUser(@Validated @RequestBody Doctor doctor) {
        return Result.succ(null);
    }

    // 编辑用户
    @PostMapping("/edit/{id}")
    @RequiresAuthentication
    public Result editUser(@RequestParam Long id) {
        return Result.succ(null);
    }

    // 失效某用户
    @GetMapping("/invalid/{userId}")
    @RequiresAuthentication
    public Result invalidUser(@RequestBody Doctor doctor) {
        return Result.succ(null);
    }

    // 查找
    @GetMapping("/find/{userId}")
    @RequiresAuthentication
    public Result FindUser(@PathVariable(name = "userId") Long userId) {
        return Result.succ(null);
    }
}