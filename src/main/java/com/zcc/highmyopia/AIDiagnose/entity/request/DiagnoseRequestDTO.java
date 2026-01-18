package com.zcc.highmyopia.AIDiagnose.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName DiagnoseRequestVO
 * @Description
 * @Author aigao
 * @Date 2026/1/17 15:41
 * @Version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagnoseRequestDTO {
    private String visitNumber;

    private Integer diseaseId;

    private Integer userId;
}
