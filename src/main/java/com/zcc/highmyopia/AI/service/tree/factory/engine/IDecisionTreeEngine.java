package com.zcc.highmyopia.AI.service.tree.factory.engine;

import com.zcc.highmyopia.AI.model.entity.DiagnoseResultEntity;

import java.util.Map;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
public interface IDecisionTreeEngine {

    DiagnoseResultEntity process(Map<String, String> url);
}
