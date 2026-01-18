package com.zcc.highmyopia.AIDiagnose.manager.modelManager.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AIDiagnose.entity.request.ModelRequestVO;
import com.zcc.highmyopia.AIDiagnose.entity.response.DiagnoseResponse;
import com.zcc.highmyopia.AIDiagnose.manager.modelManager.AIModelClient;
import com.zcc.highmyopia.common.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.zcc.highmyopia.AIDiagnose.manager.utils.HttpPOST;

/**
 * @ClassName AIModelClientImpl
 * @Description
 * @Author aigao
 * @Date 2026/1/18 21:45
 * @Version 1.0
 */
@Component
@Slf4j
public class AIModelClientImpl implements AIModelClient {

    @Value("${hospital.flask_path}")
    private String flaskPath;
    @Override
    public List<DiagnoseResponse> callModel(String url, ModelRequestVO modelRequest) {
        // 发送API请求后端flask模型服务(模拟并得到结果)
        // 请求参数为filePath
        String api = flaskPath + ":" + url;
        log.info("request api {}", api);

        String json = JSON.toJSONString(modelRequest);
        String body = HttpPOST(api, json);
        JSONObject root = JSON.parseObject(body);
        JSONArray dataArray = root.getJSONArray("data");
        if (dataArray == null) {
            return Collections.emptyList();
        }
        // 转成 List<DiagnoseResponse>
        return dataArray.toJavaList(DiagnoseResponse.class);
    }


}
