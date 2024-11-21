package com.zcc.highmyopia.util;

import java.util.Map;

/**
 * Created by mac on 16/6/17.
 */
public class SmsHttpApi {
    //    private static final String host = "http://code.58yhkj.com:7862/sms?action=%s";
    private static final String host = "http://sms.58yhkj.com/sms.aspx?action=%s";
//    private static final String host = "http://sms.58yhkj.com/sms.aspx?action=send";
//    private static final String host = "http://114.215.153.48:8803/sms.aspx?action=send";

    private ApiRequestMethodEnum method;

    public SmsHttpApi(ApiRequestMethodEnum methodEnum){
        method = methodEnum;
    }

    private static String getUrl(SmsActionEnum action)
    {
        return String.format(host,action.getDes());
    }

    private static String getUrl(SmsActionEnum action,String params)
    {
        StringBuffer urlBuffer = new StringBuffer(getUrl(action));
        urlBuffer.append("&").append(params);
        return urlBuffer.toString();
    }

    private HttpRequestor httpRequestor(){
        return new HttpRequestor();
    }

    public String doAction(Map<String,Object> params,SmsActionEnum smsActionEnum) throws Exception{
        HttpRequestor requestor = httpRequestor();

        if(method == ApiRequestMethodEnum.get)
            return requestor.doGet(getUrl(smsActionEnum,requestor.parseParamsForMap(params)));
        else
            return requestor.doPost(getUrl(smsActionEnum),params);
    }

}
