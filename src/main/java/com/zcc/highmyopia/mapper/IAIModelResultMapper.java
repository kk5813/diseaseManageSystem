package com.zcc.highmyopia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcc.highmyopia.po.AIModelResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface IAIModelResultMapper extends BaseMapper<AIModelResult> {
   List<AIModelResult> selectByCondition(@Param("patientId") Long patientId,@Param("visitNumber") String visitNumber,
                                         @Param("description") String description, @Param("userId")Long userId,
                                         @Param("diagnosisProcessId") Long diagnosisProcessId);
}
