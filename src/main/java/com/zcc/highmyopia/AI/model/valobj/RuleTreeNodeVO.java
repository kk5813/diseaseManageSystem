package com.zcc.highmyopia.AI.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description 规则树节点对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeVO {

    /** 模型ID */
    private Integer id;
    /** 规则Key */
    private String api;
    /** 规则比值 */
    private String name;

    /** 规则连线 */
    private List<RuleTreeNodeLineVO> treeNodeLineVOList;

}