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

    // 就诊信息
    @Test
    void test_GetVisit() throws Exception {
        List<VisitEntity> patientVisit = downLoadService.getVisits("20240603", "20240730");
        System.out.println(patientVisit);
    }

    // 患者信息
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

    // 处方信息 y有问题
    @Test
    void test_GetRecipe() throws Exception {
        //downLoadService.getRecipe("20240801", "20241125");
        downLoadService.getRecipe("20240603", "20240730");
    }

    // 检验结果
    @Test
    void test_GetCheckResult() throws Exception {
        // downLoadService.getCheckResult("2024-08-01", "2024-11-15");
        downLoadService.getCheckResult("2024-06-03", "2024-07-30");
    }

    // 门诊信息
    @Test
    void test_GetElement() throws Exception {
//        List<Visits> list = visitsService.list();
//        List<Visits> values = new ArrayList<>(list.stream()
//                .collect(Collectors.toMap(Visits::getVisitNumber, e -> e, (existing, replacement) -> existing))
//                .values());
//        log.info("visitNumber_num:{}", values.size());
//        values.forEach(e -> {
//            try {
//                downLoadService.getOutElementByVisitNumber("2024-06-03",
//                        "2024-07-30",
//                        e.getVisitNumber());
//            } catch (Exception ex) {
//                throw new RuntimeException(ex);
//            }
//        });
          downLoadService.getOutElementByVisitNumber("2024-06-26",
                        "20240725",
                        "MZ202407071064");
    }

    // timeout问题
    @Test
    void getElementVision(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        List<Visits> list = visitsService.list();
        List<Visits> values = new ArrayList<>(list.stream()
                .collect(Collectors.toMap(Visits::getVisitNumber, e -> e, (existing, replacement) -> existing))
                .values());
        log.info("visitNumber_num:{}", values.size());
        values.forEach(e -> {
            try {
                downLoadService.getElementVisionByVisitNumber("2024-06-11",
                        "2024-07-10",
                        e.getVisitNumber());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    // 单独保存单个用户的报告
    @Test
    void test_getCheckReport() throws Exception {

        List<Visits> list = visitsService.list();
        List<Visits> values = new ArrayList<>(list.stream()
                .collect(Collectors.toMap(Visits::getVisitNumber, e -> e, (existing, replacement) -> existing))
                .values());
        log.info("visitNumber_num:{}", values.size());
        values.forEach(e -> {
            try {
                downLoadService.getCheckReportByPatientId("2024-06-03",
                        "2024-07-30",
                        String.valueOf(e.getPatientId()));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    // 单独保存日期范围内的
    @Test
    void test_addCheckResult() {
        LocalDateTime start = LocalDateTime.now();
        System.out.println(start);
        downLoadService.getCheckReport("20240801", "20240801");
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





}
