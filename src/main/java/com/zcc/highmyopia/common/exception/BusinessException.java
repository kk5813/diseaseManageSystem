package com.zcc.highmyopia.common.exception;

import com.zcc.highmyopia.common.lang.ResultCode;

public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode errorCode) {
        super(errorCode.getInfo());
        this.code = errorCode.getCode();
    }

    public BusinessException(ResultCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
