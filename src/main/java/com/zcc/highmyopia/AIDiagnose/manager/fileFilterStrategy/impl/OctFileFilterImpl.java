package com.zcc.highmyopia.AIDiagnose.manager.fileFilterStrategy.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AIDiagnose.entity.request.ModelRequestVO;
import com.zcc.highmyopia.AIDiagnose.entity.response.DiagnoseResponse;
import com.zcc.highmyopia.AIDiagnose.manager.fileFilterStrategy.FileFilter;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.common.exception.BusinessException;
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
        log.info("返回结果:" + body);

        // 简化解析 - 直接获取OCT路径
        JSONObject root = JSON.parseObject(body);
        Integer code = root.getInteger("code");

        if (code == 201) {
            throw new BusinessException(400, "该用户无诊断所需的" + OCTType.get(config.get("OCTType")));
        }

        // 直接获取路径：root -> data -> urls -> OCT
        String octPath = root.getJSONObject("data")
                .getJSONObject("urls")
                .getString("OCT");

        return Collections.singletonList(octPath);
    }

    @Override
    public String getStrategyType() {
        return Constants.FileFilterType.OCT.getType();
    }

    public final HashMap<String, String> OCTType = new HashMap<String, String>(){{
        put("0", "眼前节OCT");
        put("1", "黄斑区OCT");
        put("2", "视盘区OCT");
        put("3", "其他OCT");
    }};

}
