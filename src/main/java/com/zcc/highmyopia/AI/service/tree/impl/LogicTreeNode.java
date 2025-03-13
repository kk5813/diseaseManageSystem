package com.zcc.highmyopia.AI.service.tree.impl;

import com.alibaba.fastjson.JSON;
import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AI.service.tree.ILogicTreeNode;
import com.zcc.highmyopia.AI.service.tree.factory.DefaultTreeFactory;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.hospital.entity.CheckReportsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
@Slf4j
@Component
public class LogicTreeNode implements ILogicTreeNode {

    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("${hospital.flask_path}")
    private static String flaskPath;

    // 处理
    @Override
    public DiagnoseResultEntity logic(Integer modelId, String filePath, String api) {

        // 发送API请求后端flask模型服务(模拟并得到结果)
        // 请求参数为filePath
//        String url = flaskPath;
//        url += api;
        log.info("url {}", api);
        // 创建请求体，封装 filePath
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("imagePath", filePath);
        String body = HttpPOST(api, jsonMap);
        return JSON.parseObject(
                Objects.requireNonNull(JSON.parseObject(body)).getJSONObject("data").toString(),
                DiagnoseResultEntity.class);
    }

    public static String HttpPOST(String api, Map<String, String> map){
        String url = flaskPath + api;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String jsonBody = JSON.toJSONString(map);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        int statusCode = response.getStatusCodeValue();
        if (statusCode != 200)
            throw new AppException(statusCode, "请求模型服务失败");
        return response.getBody();
    }
}
