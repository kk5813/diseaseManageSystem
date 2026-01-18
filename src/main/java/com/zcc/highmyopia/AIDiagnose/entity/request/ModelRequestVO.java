package com.zcc.highmyopia.AIDiagnose.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ModelRequestVO
 * @Description
 * @Author aigao
 * @Date 2026/1/17 15:42
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelRequestVO {
    private String visitNumber;

    private Map<String, List<String>> imagePaths;

    private Map<String,String> config;
    /**
     * "OctType":number;
     */
}
