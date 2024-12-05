package com.zcc.highmyopia.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * @Author zcc
 * @Date 2024/12/05
 * @Description
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppException extends RuntimeException {

    private static final long serialVersionUID = 5317680961212299217L;

    /** 异常码 */
    private int code;

    /** 异常信息 */
    private String info;

    public AppException(int code) {
        this.code = code;
    }

    public AppException(int code, Throwable cause) {
        this.code = code;
        super.initCause(cause);
    }

    public AppException(int code, String message) {
        this.code = code;
        this.info = message;
    }

    public AppException(int code, String message, Throwable cause) {
        this.code = code;
        this.info = message;
        super.initCause(cause);
    }

    @Override
    public String toString() {
        return "com.zcc.x.api.types.exception.XApiException{" +
                "code='" + code + '\'' +
                ", info='" + info + '\'' +
                '}';
    }

}
