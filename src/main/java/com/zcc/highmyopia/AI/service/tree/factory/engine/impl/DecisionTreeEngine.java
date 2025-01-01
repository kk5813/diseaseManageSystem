package com.zcc.highmyopia.AI.service.tree.factory.engine.impl;

import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeNodeLineVO;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeNodeVO;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeVO;
import com.zcc.highmyopia.AI.service.tree.ILogicTreeNode;
import com.zcc.highmyopia.AI.service.tree.factory.engine.IDecisionTreeEngine;
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
    public List<DiagnoseResultEntity> process(Map<String, String> url) {
        List<DiagnoseResultEntity> diagnoseResultEntities = new ArrayList<>();
        //获取基础信息
        Integer nextNode = ruleTreeVO.getStartNode();
        Map<Integer, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();
        while (nextNode != null){
            RuleTreeNodeVO ruleTreeNodeVO = treeNodeMap.get(nextNode);
            DiagnoseResultEntity diagnoseResult = logicTreeNode.logic(nextNode,
                    url.get(ruleTreeNodeVO.getInput()),
//                    null,
                    ruleTreeNodeVO.getApi());
            diagnoseResultEntities.add(diagnoseResult);

            nextNode = nextNode(diagnoseResult.getResultInfo(), ruleTreeNodeVO.getTreeNodeLineVOList());

        }
        return diagnoseResultEntities;
    }

    // 决策
    private Integer nextNode(String result, List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList){
        if (ruleTreeNodeLineVOList == null || ruleTreeNodeLineVOList.isEmpty()) return null;
        for (RuleTreeNodeLineVO nodeVO : ruleTreeNodeLineVOList) {
            // 结果集
            if (nodeVO.getLimitValue().contains(result))
                return nodeVO.getModelTo();
        }
        return null;
    }
}
