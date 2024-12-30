package com.zcc.highmyopia.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcc.highmyopia.mapper.IAIModelResultMapper;
import com.zcc.highmyopia.po.AIModelResult;
import com.zcc.highmyopia.service.IAIModelResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AIModelResultServiceImpl extends ServiceImpl<IAIModelResultMapper, AIModelResult> implements IAIModelResultService {

    @Autowired
    private IAIModelResultMapper aiModelResultMapper;

    @Override
    public IPage<AIModelResult> selectByCondition(Page<AIModelResult> page, Long patientId, String visitNumber, String description, Long userId, Long diagnosisProcessId) {
        List<AIModelResult> aiModelResults = aiModelResultMapper.selectByCondition(patientId, visitNumber, description, userId, diagnosisProcessId);
        page.setRecords(aiModelResults);
        return page;
    }

    @Override
    public boolean saveAIModelResult(AIModelResult aiModelResult) {
        return save(aiModelResult);
    }
}
