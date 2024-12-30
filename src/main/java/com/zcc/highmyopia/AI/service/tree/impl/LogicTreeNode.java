package com.zcc.highmyopia.AI.service.tree.impl;

import com.alibaba.fastjson.JSON;
import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AI.service.tree.ILogicTreeNode;
import com.zcc.highmyopia.AI.service.tree.factory.DefaultTreeFactory;
import com.zcc.highmyopia.common.exception.AppException;
import com.zcc.highmyopia.hospital.entity.CheckReportsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
@Slf4j
@Component
public class LogicTreeNode implements ILogicTreeNode {

    private final RestTemplate restTemplate = new RestTemplate();

    // 处理
    @Override
    public DiagnoseResultEntity logic(Integer modelId, String filePath, String api) {

        // 发送API请求后端flask模型服务(模拟并得到结果)
        // 请求参数为filePath
        String url = "http://127.0.0.1:4523/m1/3365319-628336-default/api/";
        url += api;
        log.info("url {}", url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        int statusCode = response.getStatusCodeValue();
        if (statusCode != 200)
            throw new AppException(statusCode, "请求模型服务失败");
        String body = response.getBody();
        System.out.println(body);
        return JSON.parseObject(
                Objects.requireNonNull(JSON.parseObject(body)).getJSONObject("data").toString(),
                DiagnoseResultEntity.class);
    }
}
