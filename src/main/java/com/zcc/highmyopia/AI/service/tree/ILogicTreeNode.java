package com.zcc.highmyopia.AI.service.tree;

import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description 归责树接口
 */
public interface ILogicTreeNode {

    DiagnoseResultEntity logic(Integer modelId, String filePath, String api, String visitNumber);

}
