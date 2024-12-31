package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcc.highmyopia.po.ModelDisease;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
public interface IModelDiseaseService extends IService<ModelDisease> {
    ModelDisease getModelDiseaseByDiseaseId(Integer diseaseId);
}
