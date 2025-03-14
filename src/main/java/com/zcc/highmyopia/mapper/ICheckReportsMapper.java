package com.zcc.highmyopia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcc.highmyopia.po.CheckReports;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author zcc
 * @Date 2024/12/17
 * @Description
 */
@Mapper
@Component
public interface ICheckReportsMapper extends BaseMapper<CheckReports> {
    int insert(CheckReports checkReports);

    List<CheckReports> getCheckReportById(Long patientId, String visitNumber);

    List<CheckReports> getCheckReportByVisitNumber(String visitNumber);
}
