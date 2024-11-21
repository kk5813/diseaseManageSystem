package com.zcc.highmyopia.util;

/**
 * Created by mac on 16/6/17.
 */
public enum SmsActionEnum {
    send("send"),
    ptp( "P2P"),
    overage("overage"),
    report("report"),
    mo("mo");

    private String des;

    private SmsActionEnum(String string)

    {
        des=string;
    }

    public String getDes()
    {
        return des;
    }
}
