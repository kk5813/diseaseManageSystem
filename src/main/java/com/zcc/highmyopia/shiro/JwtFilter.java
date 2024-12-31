package com.zcc.highmyopia.shiro;

import cn.hutool.core.date.StopWatch;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.zcc.highmyopia.common.exception.BusinessException;
import com.zcc.highmyopia.common.lang.Result;
import com.zcc.highmyopia.common.lang.ResultCode;
import com.zcc.highmyopia.service.TokenBlackListService;
import com.zcc.highmyopia.util.JwtUtils;
import com.zcc.highmyopia.util.ThrowUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    TokenBlackListService tokenBlackListService;

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization");
        if(StringUtils.isEmpty(jwt)) {
            return null;
        }
        return new JwtToken(jwt);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 允许 Swagger 和 Knife4j 路径放行直接放行
        // todo 发行时把下面的代码删了，这里是方便测试
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/swagger-ui.html") || requestURI.startsWith("/swagger-resources")
                || requestURI.startsWith("/v3/api-docs") || requestURI.startsWith("/doc.html")) {
            return true;
        }
        String jwt = request.getHeader("Authorization");
        if(StringUtils.isEmpty(jwt)) {
            return true;
        } else {
            // 校验jwt
            Claims claim = jwtUtils.getClaimByToken(jwt);
            if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
                throw new ExpiredCredentialsException("token已失效，请重新登录");
            }
            ThrowUtils.throwIf(tokenBlackListService.isInBlackList(jwt), new BusinessException(ResultCode.USER_LOGOUT));
            // 执行登录
            return executeLogin(servletRequest, servletResponse);
        }
    }
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        Result result = Result.fail(throwable.getMessage());
        String json = JSONUtil.toJsonStr(result);

        try {
            httpServletResponse.getWriter().print(json);
        } catch (IOException ioException) {

        }
        return false;
    }
    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }


    /**
     * Todo 下面的代码只实现了获取当前请求信息，而且没有读json数据，request.getReader只能调用一次。aop会失败，原因和shiro的拦截链有关
     * Todo 后续要么加，要么删
     * */
    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        // 获取请求相关信息
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        String requestId = UUID.randomUUID().toString();
        String url = httpServletRequest.getRequestURI();
        String ip = httpServletRequest.getRemoteHost();

        // 初始化请求参数
        String reqParam = "";
        // 处理查询参数（路径参数、查询参数）
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (parameterMap != null && !parameterMap.isEmpty()) {
            reqParam = "Query Params: " + parameterMapToString(parameterMap);
        }
        // 输出请求日志
        log.info("Request start，id: {}, path: {}, ip: {}, params: {}", requestId, url, ip, reqParam);

        // 调用父类方法继续执行过滤器链
        boolean continueChain = super.onPreHandle(request, response, mappedValue);

        // 返回是否继续执行过滤器链
        return continueChain;
    }

    /**
     * 将请求的查询参数（路径或查询参数）转换为可读字符串格式
     */
    private String parameterMapToString(Map<String, String[]> parameterMap) {
        return parameterMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + Arrays.toString(entry.getValue()))
                .collect(Collectors.joining(", "));
    }

}
