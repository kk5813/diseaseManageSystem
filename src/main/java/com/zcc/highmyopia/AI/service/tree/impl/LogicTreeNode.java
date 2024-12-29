package com.zcc.highmyopia.AI.service.tree.impl;

import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AI.service.tree.ILogicTreeNode;
import com.zcc.highmyopia.AI.service.tree.factory.DefaultTreeFactory;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
public class LogicTreeNode implements ILogicTreeNode {

    // 处理
    @Override
    public DiagnoseResultEntity logic(Integer modelId) {

        // 发送API请求后端flask模型服务(模拟并得到结果)
        String result = "1";

        return null;
    }
}
