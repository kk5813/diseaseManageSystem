package com.zcc.highmyopia.util;

import java.util.HashMap;
import java.util.Map;

public class SmsTest {
    public static void main(String[] args) {
        Map<String,Object> p = new HashMap<String,Object>();
        p.put("userid", "9606");
        p.put("account", "cddqae");
//账号
        p.put("password", "123345");
        //密码
        p.put("mobile", "17780613412");
        p.put("content","【成都爱尔眼科】这是一条测试短信");
        //接入码
        SmsHttpApi smsHttpApi = new SmsHttpApi(ApiRequestMethodEnum.get);
        try {
            //发短信
            String result = "";
            System.out.println("发送短信-------------------------------------------");
            result = smsHttpApi.doAction(p, SmsActionEnum.send);
            System.out.println(result);

            //查询余额
//            System.out.println("查询余额-------------------------------------------");
//            result = smsHttpApi.doAction(p,SmsActionEnum.overage);
//            System.out.println(result);
//
//            //上行接口(回复)
//            System.out.println("上行接口(回复)-------------------------------------------");
//            result = smsHttpApi.doAction(p,SmsActionEnum.mo);
//            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        //
    }
}
