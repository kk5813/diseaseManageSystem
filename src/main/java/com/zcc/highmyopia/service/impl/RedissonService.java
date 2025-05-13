package com.zcc.highmyopia.service.impl;
import com.zcc.highmyopia.service.IRedisService;
import org.redisson.api.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * @Author zcc
 * @Date 2024/12/04
 * @Description
 */
@Service("redissonService")
public class RedissonService implements IRedisService {

    @Resource
    private RedissonClient redissonClient;

    @Override
    public <T> void setValue(String key, T value) {
        redissonClient.<T>getBucket(key).set(value);
    }

    @Override
    public <T> void setValue(String key, T value, long expired) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value, Duration.ofMillis(expired));
    }

    @Override
    public <T> T getValue(String key) {
        return redissonClient.<T>getBucket(key).get();
    }

    @Override
    public void remove(String key) {
        redissonClient.getBucket(key).delete();
    }

    @Override
    public boolean isExist(String key) {
        RBucket<String>  bucket = redissonClient.getBucket(key);
        if (bucket == null || !bucket.isExists()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public <T> boolean isExistWithSet(String key, T value, long expired) {
        RBucket<String>  bucket = redissonClient.getBucket(key);
        if (bucket == null || !bucket.isExists()) {
            setValue(key, value, expired);
            return false;
        } else {
            return true;
        }
    }
}

