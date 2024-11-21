package com.zcc.highmyopia.util;

import java.util.Random;

public class SaltUtil {

    public static String getSalt(int n) {
        char[] dict = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890").toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; i++) {
            char c = dict[new Random().nextInt(dict.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static String getSalt() {
        char[] dict = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890").toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 32 ; i++) {
            char c = dict[new Random().nextInt(dict.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}
