package com.zcc.highmyopia.AI.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description 规则树节点指向线对象。用于衔接 from->to 节点链路关系
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeNodeLineVO {

    /** 规则树ID */
    private Integer id;
    /** 规则Key节点 From */
    private Integer modelFrom;
    /** 规则Key节点 To */
    private Integer modelTo;
    /** 限定值（到下个节点） */
    private String limitValue;

}