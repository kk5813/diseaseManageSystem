package com.zcc.highmyopia.hospital.utils;

/**
 * @Author zcc
 * @Date 2024/11/18
 * @Description
 */
public class SystemHeader {
    //签名Header
    public static final String X_CA_SIGNATURE = "Signature";
    //请求时间戳
    public static final String X_CA_TIMESTAMP = "Timestamp";
    //请求放重放Nonce,15分钟内保持唯一,建议使用UUID
    public static final String X_CA_NONCE = "AccessNonce";
    //access KEY
    public static final String X_CA_KEY = "Accesskey";
    //版本号
    public static final String X_CA_VERSION = "Version";
    //医院编号
    public static final String X_CA_HOSTID = "HospId";
}
