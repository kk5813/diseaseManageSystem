package com.zcc.highmyopia.hospital.utils;

/**
 * @Author zcc
 * @Date 2024/11/18
 * @Description
 */
public class HttpHeader {
    // HTTP Accept 头部字段，用于指定客户端能够接收的内容类型
    public static final String HTTP_HEADER_ACCEPT = "Accept";

    // HTTP Content-MD5 头部字段，用于校验消息体内容是否完整
    public static final String HTTP_HEADER_CONTENT_MD5 = "Content-MD5";

    // HTTP Content-Type 头部字段，用于指定消息体的 MIME 类型
    public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

    // HTTP Date 头部字段，用于指定请求发送的时间
    public static final String HTTP_HEADER_DATE = "Date";

    // 根据实际需求还可以包含更多的 HTTP 头部字段定义...
}
