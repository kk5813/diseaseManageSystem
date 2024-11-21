package com.zcc.highmyopia.common.lang;

import lombok.Data;

/**
 * 定义API响应结果的封装类
 * 包含响应的状态码、消息、以及返回的数据
 */
@Data  // Lombok注解，自动生成getter、setter、toString等方法
public class Result {

    private int code;  // 状态码，通常为200（成功）或400（失败）
    private String msg;  // 返回的消息提示
    private Object data;  // 返回的具体数据，可以是任意类型

    /**
     * 成功的响应，默认消息为 "操作成功"
     * @param data 返回的数据
     * @return 成功的 Result 对象
     */
    public static Result succ(Object data) {
        return succ("操作成功", data);  // 调用带消息的成功方法
    }

    /**
     * 成功的响应，允许自定义消息
     * @param mess 自定义的消息
     * @param data 返回的数据
     * @return 成功的 Result 对象
     */
    public static Result succ(String mess, Object data) {
        Result m = new Result();
        m.setCode(ResultCode.SUCCESS.getCode());  // 设置状态码为200，表示成功
        m.setData(data);  // 设置返回的数据
        m.setMsg(mess);  // 设置自定义的消息
        return m;
    }

    /**
     * 失败的响应，默认状态码为400，允许自定义消息
     * @param mess 自定义的错误消息
     * @return 失败的 Result 对象
     */
    public static Result fail(String mess) {
        return fail(ResultCode.BAD_REQUEST.getCode(), mess, null);  // 调用带状态码的失败方法，默认数据为null
    }

    /**
     * 失败的响应，允许自定义消息和返回数据
     * @param mess 自定义的错误消息
     * @param data 返回的数据
     * @return 失败的 Result 对象
     */
    public static Result fail(String mess, Object data) {
        return fail(ResultCode.BAD_REQUEST.getCode(), mess, data);  // 调用带状态码的失败方法
    }

    /**
     * 失败的响应，允许自定义状态码、消息和返回数据
     * @param code 自定义的状态码
     * @param mess 自定义的错误消息
     * @param data 返回的数据
     * @return 失败的 Result 对象
     */
    public static Result fail(int code, String mess, Object data) {
        Result m = new Result();
        m.setCode(code);  // 设置自定义状态码
        m.setData(data);  // 设置返回的数据
        m.setMsg(mess);  // 设置自定义的消息
        return m;
    }
}
