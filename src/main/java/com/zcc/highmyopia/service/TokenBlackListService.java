package com.zcc.highmyopia.service;

/**
 * @ClassName TokenBlackListService
 * @Description
 * @Author aigao
 * @Date 2024/12/31 13:35
 * @Version 1.0
 */
public interface TokenBlackListService {
    public void addToBlackList(String token, long expiration);

    public boolean isInBlackList(String token);
    public void removeBlackList(String token);
}
