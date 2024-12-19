package com.zcc.highmyopia.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zcc.highmyopia.po.Patients;
import com.zcc.highmyopia.po.ReportFiles;
import com.zcc.highmyopia.service.IReportFilesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName PatientMapperTest
 * @Description
 * @Author aigao
 * @Date 2024/12/5 15:46
 * @Version 1.0
 */

@SpringBootTest
public class ReportFilesMapperTest {
    @Autowired
    private IReportFilesService reportFilesService;

    @Resource
    private IReportFilesMapper reportFilesMapper;
    @Test
    public void test_report(){
//        List<ReportFiles> reportFilesList = reportFilesService.list(new LambdaQueryWrapper<ReportFiles>()
//                .eq(ReportFiles::getReportId, 111L));
//        System.out.println(reportFilesList);
//        Long reportId = 111L;
//        List<ReportFiles> reportFiles = reportFilesMapper.queryBatch(reportId);
        List<ReportFiles> notDownLoad = reportFilesMapper.getNotDownLoad();
        System.out.println(notDownLoad);
    }
}
