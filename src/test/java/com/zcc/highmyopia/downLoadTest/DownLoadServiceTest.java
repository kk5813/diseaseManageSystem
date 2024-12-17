package com.zcc.highmyopia.downLoadTest;

import com.zcc.highmyopia.hospital.service.IDownLoadService;
import com.zcc.highmyopia.mapper.IReportFilesMapper;
import com.zcc.highmyopia.po.ReportFiles;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description
 * @Author zcc
 * @Date 2024/12/5 15:46
 * @Version 1.0
 */
@SpringBootTest
public class DownLoadServiceTest {
    @Resource
    private IDownLoadService downLoadService;
    @Test
    void test_downLoadImage(){
        String url = "/api/report/file?requestType=WADO&studyUID=1.2.156.112817.155934683686344160769490832225524523102&seriesUID=1.2.156.112817.118358943037663121398112686282646406849&objectUID=1.2.156.112817.32109209785540512244365408321069489508";
        ReportFiles reportFiles = ReportFiles.builder()
                .id(1L)
                .reportId(111L)
                .isDownLoad((short) 0)
                .fileUrl(url)
                .fileType("application/pdf")
                .build();
        downLoadService.DownLoadReportImage(reportFiles);
    }

    @Resource
    private IReportFilesMapper reportFilesMapper;
    @Test
    void test_query(){
        List<ReportFiles> notDownLoad = reportFilesMapper.getNotDownLoad();
        System.out.println(notDownLoad);
    }


}
