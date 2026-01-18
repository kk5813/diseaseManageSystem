package com.zcc.highmyopia.AIDiagnose.entity.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @ClassName DiagnoseResponseDTO
 * @Description
 * @Author aigao
 * @Date 2026/1/17 15:43
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagnoseResponse {
    private String site;

    private String result;

    private String resultInfo;

    private Map<String, String> urls;
}
/**
 * {
 *         "site": "右眼",
 *         "result": "黄斑水肿一期",
 *         "resultInfo": "诊断的理由",
 *         "urls": {
 *             "原图":"url",
 *             "分割":"url1",
 *             "检测1":"url2",
 *             "检测2":"url3",
 *             "检测3":"url4",
 *             "检测4":"url5"
 *         }
 *     }
 */