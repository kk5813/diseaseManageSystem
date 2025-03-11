package com.zcc.highmyopia.getData;

import com.zcc.highmyopia.hospital.service.IDownLoadService;
import com.zcc.highmyopia.hospital.service.IGetDataService;
import com.zcc.highmyopia.hospital.service.impl.GetDataService;
import com.zcc.highmyopia.hospital.task.GetDataTask;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.mockito.Mockito.verify;

@SpringBootTest
public class GetDataTaskTest {



    @Autowired
    private GetDataService getDataService;

    private static final Logger log = LoggerFactory.getLogger(GetDataTaskTest.class);
    @Resource
    private IDownLoadService downLoadService;
    @Test
    public void testExec_ShouldCallGetTodayData() throws Exception {
        // 执行 exec 方法
        //getDataService.getDataTest("20241126","20241127");
        //System.out.println(getDataService.getDataToday());
        getDataService.saveRecipeInfo();
//        try{
//            downLoadService.DownLoadReportImageBatch();
//        }catch (Exception e){
//            log.error("批量导入图片到本地发生异常",e);
//        }
    }
}