package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.common.Constants;
import com.zcc.highmyopia.mapper.IModelDiseaseMapper;
import com.zcc.highmyopia.po.ModelDisease;
import com.zcc.highmyopia.service.IModelDiseaseService;
import com.zcc.highmyopia.service.IRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
@Service
public class ModelDiseaseService extends ServiceImpl<IModelDiseaseMapper, ModelDisease> implements IModelDiseaseService {

    @Resource
    private IModelDiseaseMapper modelDiseaseMapper;

    @Override
    public ModelDisease getModelDiseaseByDiseaseId(Integer diseaseId) {
        return modelDiseaseMapper.selectById(diseaseId);
    }
}
