package com.zcc.highmyopia.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcc.highmyopia.po.AIModelResult;

public interface IAIModelResultService extends IService<AIModelResult> {
    IPage<AIModelResult> selectByCondition(Page<AIModelResult> page, Long patientId, String visitNumber, String description, Long userId, Long diagnosisProcessId);
    
    boolean saveAIModelResult(AIModelResult aiModelResult);
}
