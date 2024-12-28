package com.zcc.highmyopia.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcc.highmyopia.common.dto.LoginDto;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.po.User;
import com.zcc.highmyopia.service.IUserService;
import com.zcc.highmyopia.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(tags = "登录管理")
@RequestMapping("/api/${app.config.api-version}/account")
public class AccountController {

    private final JwtUtils jwtUtils;  // JWT 工具类，用于生成和解析 JWT Token

    private final IUserService userService;  // 用户服务，用于用户相关操作
    public final static Integer USER_DELETE = -1;

    /**
     * 用户登录接口
     *
     * @param loginDto 登录数据传输对象，包含用户名和密码
     * @param response HTTP 响应对象，用于设置响应头
     * @return 登录结果
     */
    @CrossOrigin  // 允许跨域请求
    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        // todo userLoginName唯一
        // 查询用户信息
        User user = userService.getOne(new QueryWrapper<User>().eq("user_login_name", loginDto.getUserLoginName()));
        Assert.notNull(user, "用户不存在");  // 确保用户存在

        // 用户被删除应该无法访问
        if(USER_DELETE.equals(user.getUserStatus())){
            return Result.fail(ResultCode.FORBIDDEN.getCode(),ResultCode.FORBIDDEN.getInfo());
        }
        // todo: 模拟前端MD5加密过程正式测试需要删掉
        // 验证密码：加盐后进行 MD5 加密
        if (!user.getUserPassword().equals(SecureUtil.md5(user.getSalt() +
                SecureUtil.md5(loginDto.getUserPassword())))) {
            return Result.fail("密码错误！");
        }

        // 生成 JWT Token
        String jwt = jwtUtils.generateToken(user.getUserId(), user.getUserStatus());
        response.setHeader("Authorization", jwt);  // 设置 Authorization 响应头
        response.setHeader("Access-Control-Expose-Headers", "Authorization");  // 允许前端访问该头信息
        log.info("jwt:" + jwt);  // 打印生成的 JWT


        // 返回用户信息和 Token
        return Result.succ(MapUtil.builder()
                .put("userId", user.getUserId())
                .put("userLoginName", user.getUserLoginName())
                .put("userName", user.getUserName())
                .map()
        );
    }

    /**
     * 用户退出接口
     *
     * @return 退出结果
     */
    @GetMapping("/logout")
    @ApiOperation(value = "登出")
    @RequiresAuthentication  // 确保用户已认证
    public Result logout() {
        log.info("用户退出");  // 打印用户退出日志
        SecurityUtils.getSubject().logout();  // 执行 Shiro 的注销操作
        return Result.succ(null);  // 返回成功结果
    }
}
