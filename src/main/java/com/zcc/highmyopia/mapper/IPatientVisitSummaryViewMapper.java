package com.zcc.highmyopia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcc.highmyopia.po.PatientVisitSummaryView;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName IPatientVisitSummaryViewMapper
 * @Description
 * @Author aigao
 * @Date 2024/12/30 17:00
 * @Version 1.0
 */
@Mapper
@Component
public interface IPatientVisitSummaryViewMapper  extends BaseMapper<PatientVisitSummaryView> {
    List<PatientVisitSummaryView> selectByVisitNumberOrPatientId(
            @Param("visitNumber") String visitNumber,
            @Param("patientId") Long patientId
    );
}
