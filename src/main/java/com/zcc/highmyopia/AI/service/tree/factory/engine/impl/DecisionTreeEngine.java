package com.zcc.highmyopia.AI.service.tree.factory.engine.impl;

import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeNodeLineVO;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeNodeVO;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeVO;
import com.zcc.highmyopia.AI.service.tree.ILogicTreeNode;
import com.zcc.highmyopia.AI.service.tree.factory.engine.IDecisionTreeEngine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description 引擎类
 */
@Slf4j
public class DecisionTreeEngine implements IDecisionTreeEngine {

    private final ILogicTreeNode logicTreeNode;

    private final RuleTreeVO ruleTreeVO;


    public DecisionTreeEngine(RuleTreeVO ruleTreeVO, ILogicTreeNode logicTreeNode) {
        this.ruleTreeVO = ruleTreeVO;
        this.logicTreeNode = logicTreeNode;
    }

    // 处理节点经过的流程
    @Override
    public List<DiagnoseResultEntity> process(Map<String, String> url, String visitNumber) {
        List<DiagnoseResultEntity> diagnoseResultEntities = new ArrayList<>();
        //获取基础信息
        nextNode nextNode = DecisionTreeEngine.nextNode.builder()
                .nextNode(ruleTreeVO.getStartNode())
                .useOutputUrl(0)
                .build();
        Map<Integer, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();
        String inputUrl = "";
        while (nextNode != null) {
            RuleTreeNodeVO ruleTreeNodeVO = treeNodeMap.get(nextNode.getNextNode());
            if (nextNode.getUseOutputUrl() == 1 && !diagnoseResultEntities.isEmpty()) {
                inputUrl = diagnoseResultEntities.get(diagnoseResultEntities.size() - 1).getUrl();
            } else
                inputUrl = url.get(ruleTreeNodeVO.getInput());
            DiagnoseResultEntity diagnoseResult = logicTreeNode.logic(nextNode.getNextNode(),
                    inputUrl,
                    ruleTreeNodeVO.getApi(),
                    visitNumber);
            diagnoseResultEntities.add(diagnoseResult);

            nextNode = nextNode(diagnoseResult.getResultInfo(), ruleTreeNodeVO.getTreeNodeLineVOList());

        }
        return diagnoseResultEntities;
    }

    // 决策
    private nextNode nextNode(String result, List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList) {
        if (ruleTreeNodeLineVOList == null || ruleTreeNodeLineVOList.isEmpty()) return null;
        for (RuleTreeNodeLineVO nodeVO : ruleTreeNodeLineVOList) {
            // 结果集
            if (nodeVO.getLimitValue().contains(result) || nodeVO.getLimitValue().equals("allow"))
                return nextNode.builder()
                        .nextNode(nodeVO.getModelTo())
                        .useOutputUrl(nodeVO.getUseOutputUrl())
                        .build();
        }
        return null;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class nextNode {

        private Integer nextNode;

        private Integer useOutputUrl;
    }
}
