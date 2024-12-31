package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zcc.highmyopia.po.ModelLine;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
public interface IModelLineService extends IService<ModelLine> {
    List<ModelLine> getModelLineByDiseaseId(Integer diseaseId);
}
