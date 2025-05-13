package com.zcc.highmyopia.service;
import org.redisson.api.*;

/**
 * @Author zcc
 * @Date 2024/12/04
 * @Description
 */
public interface IRedisService {

    /**
     * 设置指定 key 的值
     *
     * @param key   键
     * @param value 值
     */
    <T> void setValue(String key, T value);

    /**
     * 设置指定 key 的值
     *
     * @param key     键
     * @param value   值
     * @param expired 过期时间
     */
    <T> void setValue(String key, T value, long expired);

    /**
     * 获取指定 key 的值
     *
     * @param key 键
     * @return 值
     */
    <T> T getValue(String key);


    /**
     * 移除指定 key 的值
     *
     * @param key 键
     */
    void remove(String key);

    /**
     * 判断指定 key 的是否存在
     *
     * @param key 键
     */
    boolean isExist(String key);

    <T> boolean isExistWithSet(String key, T value, long expired);


}

