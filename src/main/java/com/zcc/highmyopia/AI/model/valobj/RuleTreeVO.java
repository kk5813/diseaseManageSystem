package com.zcc.highmyopia.AI.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Map;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description 规则树对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleTreeVO {

    /** 规则树ID */
    private Integer id;
    /** 规则树名称 */
    private String name;
    /** 规则树描述 */
    private String desc;
    /** 规则根节点 */
    private Integer startNode;

    private String input;

    /** 规则节点 */
    private Map<Integer, RuleTreeNodeVO> treeNodeMap;

}