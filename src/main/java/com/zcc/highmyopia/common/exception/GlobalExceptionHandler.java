package com.zcc.highmyopia.common.exception;

import com.zcc.highmyopia.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

/**
 * 全局异常处理类
 * 使用 @RestControllerAdvice 为全局的控制器类提供异常处理功能
 * 通过 @ExceptionHandler 捕获不同类型的异常，并返回自定义的响应
 */
@Slf4j  // Lombok注解，自动生成日志对象 log，用于记录日志
@RestControllerAdvice  // 标识该类为全局异常处理类，应用于所有的控制器
public class GlobalExceptionHandler {

    /**
     * 捕捉 Shiro 的未授权异常
     * 返回 401 未授权状态码和自定义的失败响应
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)  // 设置响应状态码为 401
    @ExceptionHandler(ShiroException.class)  // 捕捉 Shiro 的异常
    public Result handle401(ShiroException e) {
        return Result.fail(401, e.getMessage(), null);  // 返回自定义的错误结果
    }

    /**
     * 捕捉 @Validated 校验失败异常
     * 例如在请求参数中使用 @Validated 进行数据校验时发生错误
     * 返回 400 错误状态码和第一个校验错误的信息
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 设置响应状态码为 400
    @ExceptionHandler(value = MethodArgumentNotValidException.class)  // 捕捉校验失败异常
    public Result handler(MethodArgumentNotValidException e) throws IOException {
        log.error("运行时异常:-------------->", e);  // 记录异常日志
        BindingResult bindingResult = e.getBindingResult();  // 获取校验结果
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();  // 获取第一个错误
        return Result.fail(objectError.getDefaultMessage());  // 返回校验错误的消息
    }

    /**
     * 捕捉 IllegalArgumentException 异常（通常用于断言失败等场景）
     * 返回 400 错误状态码和自定义的失败响应
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 设置响应状态码为 400
    @ExceptionHandler(value = IllegalArgumentException.class)  // 捕捉 IllegalArgumentException 异常
    public Result handler(IllegalArgumentException e) throws IOException {
        log.error("Assert异常:-------------->{}", e.getMessage());  // 记录错误日志
        return Result.fail(e.getMessage());  // 返回自定义的错误结果
    }

    /**
     * 捕捉运行时异常 RuntimeException
     * 返回 400 错误状态码和自定义的失败响应
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 设置响应状态码为 400
    @ExceptionHandler(value = RuntimeException.class)  // 捕捉运行时异常
    public Result handler(RuntimeException e) throws IOException {
        log.error("运行时异常:-------------->", e);  // 记录异常日志
        return Result.fail(e.getMessage());  // 返回自定义的错误结果
    }
}
