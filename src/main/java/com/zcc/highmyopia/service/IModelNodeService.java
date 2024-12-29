package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcc.highmyopia.po.ModelNode;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
public interface IModelNodeService extends IService<ModelNode> {
    List<ModelNode> getModelNodeByDiseaseId(Integer diseaseId);
}
