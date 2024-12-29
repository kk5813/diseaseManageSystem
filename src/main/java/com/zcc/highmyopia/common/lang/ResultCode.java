package com.zcc.highmyopia.common.lang;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Author zcc
 * @Date 2024/12/04
 * @Description
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResultCode {
    SUCCESS(200,"服务器成功返回用户请求的数据"),
    CREATED(201,"用户新建或修改数据成功"),
    Accepted(202,"表示一个请求已经进入后台排队（异步任务）"),
    BAD_REQUEST(400, "用户发出的请求有错误，服务器没有进行新建或修改数据的操作"),
    UNAUTHORIZED(401, "用户没有权限"),
    FORBIDDEN(403, "表示用户得到授权（与401错误相对），但是访问是被禁止的"),
    NOT_FOUND(404, "用户发出的请求针对的是不存在的记录，服务器没有进行操作。"),
    NOT_ACCEPTABLE(406, "用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。"),
    GONE(410, "用户请求的资源被永久删除，且不会再得到的。"),
    INTERNAL_SERVER_ERROR(500, "服务器发生错误，用户将无法判断发出的请求是否成功"),

    // 五位数的异常代码，为业务异常代码。以后尽量用这个吧，自动定义，自己说明。
    USER_EDIT_ERROR(40003,"登录用户名已存在，操作失败"),
    PARAMS_ERROR(40000, "请求params参数错误"),
    OPERATION_ERROR(50001, "操作失败"),
    RESOURCE_NOT_FOUND(40404,"请求的图片资源不存在"),
    PATHVARIABLE_NULL(40002, "请求的路径参数为空"),
    FILE_FORMAT_ERROR(40001,"文件格式非法"),

    ID_NOT_FOUND(40401, "不存在指向该id的数据！"),
    DATE_VARIABLE_ERROR(40004,"日期参数错误，也可能传入的参数逻辑上错误"),
    ;

    private int code;
    private String info;

}
