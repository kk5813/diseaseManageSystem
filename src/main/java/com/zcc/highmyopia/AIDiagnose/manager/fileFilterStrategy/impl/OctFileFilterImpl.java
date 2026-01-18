package com.zcc.highmyopia.AIDiagnose.manager.fileFilterStrategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zcc.highmyopia.AIDiagnose.entity.request.ModelRequestVO;
import com.zcc.highmyopia.AIDiagnose.entity.response.DiagnoseResponse;
import com.zcc.highmyopia.AIDiagnose.manager.fileFilterStrategy.FileFilter;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.po.ReportFiles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

import static com.zcc.highmyopia.AIDiagnose.manager.utils.HttpPOST;

/**
 * @ClassName OctFileFilterImpl
 * @Description
 * @Author aigao
 * @Date 2026/1/17 15:55
 * @Version 1.0
 */
@Component
@Slf4j
public class OctFileFilterImpl implements FileFilter {
    private final String OCT_CLASSIFY_API = "/api/oct_classify";

    private final String port = "4092";

    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("${hospital.flask_path}")
    private String flaskPath;

    @Override
    public List<String> filterFiles(List<ReportFiles> files, Map<String, String> config) {
        String api = flaskPath + ":" + port + OCT_CLASSIFY_API;
        log.info("request api {}", api);
        Map<String, List<String>> requestMap = new HashMap<>();
        requestMap.put(getStrategyType(), files.stream()
                .filter(reportFiles -> !"application/pdf".equals(reportFiles.getType()))
                .map(ReportFiles::getFilePath).collect(Collectors.toList()));
        String visitNumber = "000000";
        ModelRequestVO modelRequest = ModelRequestVO.builder()
                .imagePaths(requestMap)
                .visitNumber(visitNumber)
                .config(config)
                .build();
        String json = JSON.toJSONString(modelRequest);
        String body = HttpPOST(api, json);
        JSONObject root = JSON.parseObject(body);
        JSONArray dataArray = root.getJSONArray("data");
        if (dataArray == null) {
            return Collections.emptyList();
        }
        // 转成 List<DiagnoseResponse>
        List<DiagnoseResponse> javaList = dataArray.toJavaList(DiagnoseResponse.class);
        return javaList.stream()
                .map(DiagnoseResponse::getUrls)
                .filter(Objects::nonNull)
                .flatMap(m-> m.values().stream())
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyType() {
        return Constants.FileFilterType.OCT.getTypeName();
    }

}
