package com.zcc.highmyopia.util;

import com.zcc.highmyopia.common.exception.BusinessException;
import com.zcc.highmyopia.common.lang.ResultCode;

/**
 * @ClassName ThrowUtils
 * @Description 抛异常工具类
 * @Author aigao
 * @Date 2024/12/24 18:10
 * @Version 1.0
 */
public class ThrowUtils {
    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     */
    public static void throwIf(boolean condition, ResultCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void throwIf(boolean condition, ResultCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }

}
