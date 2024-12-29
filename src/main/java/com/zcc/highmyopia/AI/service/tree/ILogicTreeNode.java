package com.zcc.highmyopia.AI.service.tree;

import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;
import com.zcc.highmyopia.AI.service.tree.factory.DefaultTreeFactory;
import org.omg.CORBA.INTERNAL;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description 归责树接口
 */
public interface ILogicTreeNode {

    DiagnoseResultEntity logic(Integer modelId);

}
