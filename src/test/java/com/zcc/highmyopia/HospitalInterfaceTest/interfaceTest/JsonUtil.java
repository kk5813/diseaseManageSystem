package com.zcc.highmyopia.HospitalInterfaceTest.interfaceTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 将 JSON 字符串转换为 Map<String, String>
    public static Map<String, String> fromJsonToMap(String json) throws Exception {
        return objectMapper.readValue(json, new TypeReference<Map<String, String>>() {});
    }

    // 将对象转换为 JSON 字符串
    public static String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }
}
