package com.zcc.highmyopia.common.lang;

/**
 * @Author zcc
 * @Date 2024/9/28
 * @Description
 */
public enum ResultCode {
    SUCCESS(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404);

    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
