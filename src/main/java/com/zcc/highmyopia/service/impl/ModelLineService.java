package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.mapper.IModelLineMapper;
import com.zcc.highmyopia.po.ModelLine;
import com.zcc.highmyopia.service.IModelLineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/29
 * @Description
 */
@Service
public class ModelLineService extends ServiceImpl<IModelLineMapper, ModelLine> implements IModelLineService {

    @Resource
    private IModelLineMapper modelLineMapper;

    @Override
    public List<ModelLine> getModelLineByDiseaseId(Integer diseaseId) {
        return modelLineMapper.selectList(new LambdaQueryWrapper<ModelLine>()
                .eq(ModelLine::getDiseaseId, diseaseId));
    }
}
