package com.zcc.highmyopia.util;

import com.zcc.highmyopia.common.Constants;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: aigao
 * @CreateTime: 2025-05-13-11:55
 * @Description:
 * @Version: 1.0
 */
public class OtherUtils {
    public static class GenerateRedisKey{
        public static String generateKey(HttpServletRequest request, String params){
            return Constants.RedisKey.REPEAT_SUBMIT + request.getRequestURI() + params.hashCode();
        }
    }
}
