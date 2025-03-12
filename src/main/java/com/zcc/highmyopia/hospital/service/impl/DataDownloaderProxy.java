package com.zcc.highmyopia.hospital.service.impl;

import com.zcc.highmyopia.hospital.service.IDownLoadDataUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName DataDownloaderProxy
 * @Description
 * @Author aigao
 * @Date 2025/3/12 13:03
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataDownloaderProxy implements InvocationHandler {

    private final IDownLoadDataUtils target;
    private static final int MAX_RETRIES = 3;

    public IDownLoadDataUtils createProxy(){
        return (IDownLoadDataUtils) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                this
        );
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        int attempt = 0;
        Throwable lastException = null;
        while (attempt < MAX_RETRIES) {
            attempt++;
            try {
                long startTime = System.currentTimeMillis();
                Object result = method.invoke(target, args);
                long endTime = System.currentTimeMillis();
                log.info("Method{} executed in {} ms", method.getName(), endTime-startTime);
                return result;
            } catch (Exception e) {
                lastException = e;
                if (isNetworkException(e)) {
                    log.info("Network error occurred, retrying... ({}/{})", attempt,MAX_RETRIES);
                } else {
                    throw e;
                }
            }
        }
        throw lastException;
    }
    private boolean isNetworkException(Exception ex) {
        // 检查是否是 ResourceAccessException
        if (ex instanceof InvocationTargetException) {
            Throwable cause = ex.getCause();
            if(cause instanceof ResourceAccessException || cause instanceof java.net.SocketTimeoutException ||
                    cause instanceof java.net.ConnectException ||
                    cause instanceof java.net.UnknownHostException ||
                    cause instanceof java.net.SocketException ||
                    cause instanceof java.io.EOFException)
            return true;
        }
        // 检查是否是其他直接的网络异常
        else if (ex instanceof java.net.SocketTimeoutException ||
                ex instanceof java.net.ConnectException ||
                ex instanceof java.net.UnknownHostException ||
                ex instanceof java.net.SocketException ||
                ex instanceof java.io.EOFException) {
            return true;
        }
        log.error("非网络异常");
        return false;
    }
}
