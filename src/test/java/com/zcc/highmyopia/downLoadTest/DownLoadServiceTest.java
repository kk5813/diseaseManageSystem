package com.zcc.highmyopia.downLoadTest;

import com.zcc.highmyopia.hospital.entity.VisitEntity;
import com.zcc.highmyopia.hospital.service.IDownLoadService;
import com.zcc.highmyopia.mapper.IReportFilesMapper;
import com.zcc.highmyopia.po.ReportFiles;
import com.zcc.highmyopia.po.Visits;
import com.zcc.highmyopia.service.IVisitsService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author zcc
 * @Date 2024/12/5 15:46
 * @Version 1.0
 */
@SpringBootTest
public class DownLoadServiceTest {
    private static final Logger log = LoggerFactory.getLogger(DownLoadServiceTest.class);
    @Resource
    private IDownLoadService downLoadService;
    @Resource
    private IReportFilesMapper reportFilesMapper;

    /**
     *   @Description : 测试医院接口PDF获取和转化为png图片
     *   @available : True
     *   @Date : 2024-12-22 22时
     * */
    @Test
    void test_downLoadImage(){
        String url = "/api/report/file?requestType=WADO&studyUID=1.2.156.112817.155934683686344160769490832225524523102&seriesUID=1.2.156.112817.118358943037663121398112686282646406849&objectUID=1.2.156.112817.32109209785540512244365408321069489508";
        ReportFiles reportFiles = ReportFiles.builder()
                .id(1L)
                .reportId(111L)
                .isDownLoad( 0)
                .url(url)
                .type("application/pdf")
                .build();
        downLoadService.DownLoadReportImage(reportFiles);
    }

    DateTimeFormatter formatterWithSplit = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter formatterNoSplit = DateTimeFormatter.ofPattern("yyyyMMdd");
    LocalDateTime today = LocalDateTime.now();
    String dataSplit = today.format(formatterWithSplit);
    String dataNoSplit = today.format(formatterNoSplit);
    @Test
    void test_GetVisit() throws Exception {
        List<VisitEntity> patientVisit = downLoadService.getPatientVisit("20240603", "20240730");
        System.out.println(patientVisit);
    }

    // 测试
    @Test
    void test_getPatientsInfo() throws Exception {
        List<Visits> list = visitsService.list();
        List<Visits> values = new ArrayList<>(list.stream()
                .collect(Collectors.toMap(Visits::getPatientId, e -> e, (existing, replacement) -> existing))
                .values());
        log.info("patient_num:{}", values.size());
        values.forEach(e -> {
            try {
                downLoadService.getPatientInfoByPatientId(String.valueOf(e.getPatientId()));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

    }

    @Test
    void test_GetRecipe() throws Exception {
        downLoadService.getRecipe("20240801", "20241125");
        //downLoadService.getRecipe("20241219", "20241219");
    }
    @Test
    void test_GetReportResult() throws Exception {
        downLoadService.getReportDetail("2024-08-01", "2024-11-15");
    }

    @Test
    void test_GetElement() throws Exception {
        downLoadService.getOutElementByVisitNumber("2024-06-26", "20240725", "MZ202407071064");
    }

    // 单独保存单个用户
    @Test
    void test_getCheckReport() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        System.out.println(start);
        downLoadService.getCheckReportByPatientId("20241219", "20241219", "1869575479859933185");
        LocalDateTime end = LocalDateTime.now();
        System.out.println(end);
    }
    @Test
    void test_addCheckResult() {
        LocalDateTime start = LocalDateTime.now();
        System.out.println(start);
        downLoadService.getCheckReport("20241219", "20241219");
        LocalDateTime end = LocalDateTime.now();
        System.out.println(end);
    }
    @Test
    void test_downLoadOne(){
        List<ReportFiles> list = reportFilesMapper.queryBatch(33L);
        System.out.println(list);
        list.forEach(l -> downLoadService.DownLoadReportImage(l));
    }

    @Test
    void test_downLoadBatch(){
        LocalDateTime start = LocalDateTime.now();
        System.out.println(start);
        downLoadService.DownLoadReportImageBatch();
        LocalDateTime end = LocalDateTime.now();
        System.out.println(end);
    }

    @Resource
    private IVisitsService visitsService;

    @Test
    void getElementVision(){
        List<Visits> list = visitsService.list();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        list.forEach( e -> {
            String visitNumber = e.getVisitNumber();
            LocalDateTime diagTime = e.getDiagTime();
            String date = diagTime.format(formatter);
            try {
                downLoadService.getElementVisionByVisitNumber(date,date, visitNumber);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

    }



}
