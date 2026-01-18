package com.zcc.highmyopia.AIDiagnose.manager;

import com.zcc.highmyopia.common.exception.AppException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName utils
 * @Description
 * @Author aigao
 * @Date 2026/1/18 22:54
 * @Version 1.0
 */
public class utils {
    private static final RestTemplate restTemplate = new RestTemplate();
    public static String HttpPOST(String url, String jsonBody){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        int statusCode = response.getStatusCodeValue();
        if (statusCode != 200)
            throw new AppException(statusCode, "请求模型服务失败");
        return response.getBody();
    }
}
