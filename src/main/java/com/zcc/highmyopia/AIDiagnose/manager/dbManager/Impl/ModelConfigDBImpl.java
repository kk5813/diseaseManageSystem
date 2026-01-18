package com.zcc.highmyopia.AIDiagnose.manager.dbManager.Impl;

import com.zcc.highmyopia.AIDiagnose.manager.dbManager.ModelConfigDB;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.po.ModelDisease;
import com.zcc.highmyopia.po.ModelNode;
import com.zcc.highmyopia.service.IModelDiseaseService;
import com.zcc.highmyopia.service.IModelNodeService;
import com.zcc.highmyopia.service.IRedisService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ModelConfigDBImpl
 * @Description
 * @Author aigao
 * @Date 2026/1/17 23:16
 * @Version 1.0
 */
@Component
@AllArgsConstructor
public class ModelConfigDBImpl implements ModelConfigDB {
    private final IRedisService redisService;
    private final IModelDiseaseService modelDiseaseService;
    private final IModelNodeService modelNodeService;
    @Override
    public List<String> getNeedCheckItem(Integer diseaseId) {
        // 先查缓存
        String cacheKey = Constants.RedisKey.MODEL_DISEASE_INPUT + diseaseId;
        List<String> checkItems = redisService.getValue(cacheKey);

        if(checkItems != null && !checkItems.isEmpty()){
            return checkItems;
        }
        // 缓存没有再查数据库
        ModelDisease modelDisease = modelDiseaseService.getModelDiseaseByDiseaseId(diseaseId);
        String input = modelDisease.getInput();
        if(input == null || input.isEmpty()){
            // 缓存10秒为空数据
            redisService.setValue(cacheKey, Collections.emptyList(), Constants.RedisCacheTime.TEN_SECOND);
        }
        // 处理结果
        List<String> collect = Arrays.stream(input.split(Constants.MODEL_INPUT_SPLIT)).collect(Collectors.toList());
        // 更新缓存,存一天24小时
        redisService.setValue(cacheKey, collect, Constants.RedisCacheTime.DEFAULT_EXPIRED);
        // 返回结果
        return collect;
    }

    @Override
    public String getUrlByDiseaseId(Integer diseaseId) {
        // 先查缓存
        String cacheKey = Constants.RedisKey.MODEL_DISEASE_URL + diseaseId;
        String url = redisService.getValue(cacheKey);
        if(StringUtils.isNotBlank(url)){
            return url;
        }
        // 缓存没有再查数据库
        ModelDisease modelDiseaseByDiseaseId = modelDiseaseService.getModelDiseaseByDiseaseId(diseaseId);
        ModelNode modelNodeByStartId = modelNodeService.getModelNodeByStartId(modelDiseaseByDiseaseId.getStartNode());
        url = modelNodeByStartId.getApi();
        if(StringUtils.isBlank(url)){
            // 缓存10秒为空数据
            redisService.setValue(cacheKey, Collections.emptyList(), Constants.RedisCacheTime.TEN_SECOND);
        }
        // 更新缓存,存一天24小时
        redisService.setValue(cacheKey,url, Constants.RedisCacheTime.DEFAULT_EXPIRED);
        return url;
    }

    public void evictCache(Integer diseaseId){
        String cacheKey = Constants.RedisKey.MODEL_DISEASE_INPUT + diseaseId;
        redisService.remove(cacheKey);
    }
}
