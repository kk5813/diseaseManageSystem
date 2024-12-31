package com.zcc.highmyopia.service.impl;

import com.zcc.highmyopia.service.TokenBlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName TokenBlackListServiceImpl
 * @Description
 * @Author aigao
 * @Date 2024/12/31 13:37
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class TokenBlackListServiceImpl implements TokenBlackListService {

    private static final String BLACKLIST_PREFIX = "blacklist:";

    @Autowired
    private RedissonService redissonService;

    @Override
    public void addToBlackList(String token, long expiration) {
        redissonService.setValue(BLACKLIST_PREFIX+token, "true", expiration );
    }

    @Override
    public boolean isInBlackList(String token) {
        return redissonService.isExist(BLACKLIST_PREFIX+token);
    }

    @Override
    public void removeBlackList(String token) {
        if(redissonService.isExist(BLACKLIST_PREFIX+token)){
            redissonService.remove(BLACKLIST_PREFIX+token);
        }
    }
}
