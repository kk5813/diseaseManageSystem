package com.zcc.highmyopia;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName CharSetTest
 * @Description
 * @Author aigao
 * @Date 2025/1/6 22:33
 * @Version 1.0
 */
public class CharSetTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "朱畅畅,男,2022-08-02T14:00";
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        System.out.println(new String(bytes, "GBK"));
    }
}
