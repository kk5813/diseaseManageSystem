package com.zcc.highmyopia.AI.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiagnoseEntity {

    private Long patientId;

    private Integer diseaseId;

}
