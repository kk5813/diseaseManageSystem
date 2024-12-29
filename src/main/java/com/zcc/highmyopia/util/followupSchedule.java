package com.zcc.highmyopia.util;

import com.zcc.highmyopia.common.vo.ListFollowupVO;
import com.zcc.highmyopia.mapper.IFollowupMapper;
import com.zcc.highmyopia.mapper.IPatientsMapper;
import com.zcc.highmyopia.service.IFollowupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class followupSchedule {
    @Autowired
    IFollowupService followupService;
    @Autowired
    IFollowupMapper followupMapper;
    @Autowired
    IPatientsMapper patientsMapper;

    //3.添加定时任务
    @Scheduled(cron = "0 0 8 * * ?")
    //或直接指定时间间隔，例如：5秒
    //@Scheduled(fixedRate=5000)
    private void configureTasks() {
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
        Map<String,Object> p = new HashMap<String,Object>();
        p.put("userid", "9606");
        p.put("account", "cddqae");
        p.put("password", "123345");
        SmsHttpApi smsHttpApi = new SmsHttpApi(ApiRequestMethodEnum.get);

        String result = "";
        System.out.println("发送短信-------------------------------------------");
        for(ListFollowupVO listFollowup : followupMapper.selectTomorrowFollowUpList()) {
            System.out.print(listFollowup.getPatientName() + " ");
            System.out.println(patientsMapper.selectTelephoneByPatientId(listFollowup.getPatientId()));
            String str = listFollowup.getPatientName();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM月dd日");
            String planVisitDate = listFollowup.getPlanVisitDate().format(formatter);

            String sstr = "【成都爱尔眼科】" + str + "您好:您已成功预约" + planVisitDate + "随访检查,请保持电话畅通.如有不详请拨85075888（带上身份证和健康证（小区出入卡）患者限定一位家属陪同，做好个人防护，）\n" +
                    "东区爱尔地址：成华区双林路388号（电视塔对面）。导航：富临大厦\n" +
                    "公交路线：5.6.8.61.76.106.80.237.1004.1009.1033\n" +
                    "地铁路线：3号线和4号线在市二医院站下车（E出口）乘坐公交（2站）至游泳池站或猛追湾站下车.详询028-85075888";
            p.put("mobile", patientsMapper.selectTelephoneByPatientId(listFollowup.getPatientId()));
            p.put("content", sstr);
            try {
                //发短信
                result = smsHttpApi.doAction(p, SmsActionEnum.send);
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
