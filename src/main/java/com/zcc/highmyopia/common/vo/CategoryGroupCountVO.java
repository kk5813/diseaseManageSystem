package com.zcc.highmyopia.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/22
 * @Description 返回前端的视图
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryGroupCountVO {

    // 疾病组名称
    private String groupName;

    // 该疾病组的人数（所有细分类别的总和）
    private Integer totalCount;

    // 该疾病组下细分类别的统计列表
    private List<CategoryCountVO> categoryCounts;


}
