package com.zcc.highmyopia.hospital.utils;

import java.nio.charset.StandardCharsets;

public class MessageDigestUtil {

    /**
     * Converts a UTF-8 string to ISO-8859-1 encoding.
     *
     * @param input The UTF-8 encoded string.
     * @return A string encoded in ISO-8859-1.
     */
    public static String utf8ToIso88591(String input) {
        if (input == null) {
            return null;
        }
        String s = new String(input.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        return s;
    }

    /**
     * Converts an ISO-8859-1 string to UTF-8 encoding.
     *
     * @param input The ISO-8859-1 encoded string.
     * @return A string encoded in UTF-8.
     */
    public static String iso88591ToUtf8(String input) {
        if (input == null) {
            return null;
        }
        String s = new String(input.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        return s;
    }
}
