package com.zcc.highmyopia.AI.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
@Data
@NoArgsConstructor
@Accessors
public class DiagnoseResultEntity {

    // 诊断结果描述信息
    String resultInfo;

    // 分割或检测的结果图URL
    String url;
}
