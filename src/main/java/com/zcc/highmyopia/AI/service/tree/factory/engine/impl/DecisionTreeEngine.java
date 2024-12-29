package com.zcc.highmyopia.AI.service.tree.factory.engine.impl;

import com.sun.org.apache.regexp.internal.RE;
import com.zcc.highmyopia.AI.model.entity.DiagnoseEntity;
import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeNodeLineVO;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeNodeVO;
import com.zcc.highmyopia.AI.model.valobj.RuleTreeVO;
import com.zcc.highmyopia.AI.repository.IDiagnoseRepository;
import com.zcc.highmyopia.AI.service.IDiagnoseService;
import com.zcc.highmyopia.AI.service.tree.ILogicTreeNode;
import com.zcc.highmyopia.AI.service.tree.factory.DefaultTreeFactory;
import com.zcc.highmyopia.AI.service.tree.factory.engine.IDecisionTreeEngine;
import com.zcc.highmyopia.po.ModelNode;
import com.zcc.highmyopia.po.ModelLine;
import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.expr.Component;

import javax.annotation.Resource;
import javax.print.DocFlavor;
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

    @Resource
    private ILogicTreeNode logicTreeNode;

    private final RuleTreeVO ruleTreeVO;


    public DecisionTreeEngine(RuleTreeVO ruleTreeVO) {
        this.ruleTreeVO = ruleTreeVO;
    }

    // 处理节点经过的流程
    @Override
    public DiagnoseResultEntity process(Map<String, String> url) {
        StringBuilder resultInfo = new StringBuilder();
        List<String> urls = new ArrayList<>();
        //获取基础信息
        Integer nextNode = ruleTreeVO.getStartNode();
        Map<Integer, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();

        while (nextNode != null){

            DiagnoseResultEntity logic = logicTreeNode.logic(nextNode);
            resultInfo.append(logic.getResultInfo());
            urls.addAll(logic.getUrls());
            RuleTreeNodeVO ruleTreeNodeVO = treeNodeMap.get(nextNode);
            nextNode = nextNode(logic.getResultInfo(), ruleTreeNodeVO.getTreeNodeLineVOList());

        }
        DiagnoseResultEntity diagnoseResult = new DiagnoseResultEntity();
        diagnoseResult.setResultInfo(new String(resultInfo));
        diagnoseResult.setUrls(urls);
        return diagnoseResult;
    }

    private Integer nextNode(String result, List<RuleTreeNodeLineVO> ruleTreeNodeVOList){
        if (ruleTreeNodeVOList == null || ruleTreeNodeVOList.isEmpty()) return null;
        for (RuleTreeNodeLineVO nodeVO : ruleTreeNodeVOList) {
            if (nodeVO.getLimitValue().equals(result))
                return nodeVO.getModelTo();
        }
        return null;
    }
}
