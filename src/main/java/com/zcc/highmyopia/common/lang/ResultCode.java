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
    ID_FIELD_MISSING(40005,"传入的参数缺失某个ID值字段"),
    PARAMS_FIELD_MISSING(40006,"json请求参数缺失，或者不完整"),
    JSON_PARSE_ERROR(40007,"json格式转化失败"),
    TOKEN_NOT_EXIST(40008,"token不存在！"),
    USER_LOGOUT(40009,"用户已经登出，请重新登录！"),
    VISIT_NUMBER_IS_NULL(40010,"visitNumber 为空，后续操作失败"),


    Y_saveByPatientID(80001,"saveByPatientID,发生异常，异常PatientID="),
    Y_saveCheckResult(80002,"saveCheckResult,保存检查结果失败"),
    Y_saveElementVision(80003,"saveElementVision,视力眼压数据保存失败"),
    Y_saveElement(80004,"saveElement,保存门诊病历失败"),
    Y_saveByVisitNumber(80005,"saveByVisitNumber,发生异常，异常VisitNumber="),
    Y_saveRecipeAndOrderDetail(80006,"saveRecipeAndOrderDetail,发生异常"),

    Z_getPatientVisit(90001, "请求/api/interface/medical/getPatientVisit 就诊接口失败！"),
    Z_getById(90002,"请求/api/interface/patientInfo/getById 敏感信息接口失败"),
    Z_getList(90003, "请求/api/report/getList 图片接口失败"),
    Z_getAutoVisionByVisitNumber(90004,"请求/avis/interface/deviceDocking/getAutoVisionByVisitNumber 视力眼压接口失败"),
    Z_getReportDetail(90005,"请求/alis/interface/reportDetail/getReportDetail 检查报告（文字）失败"),
    Z_getOutpElementByCondition(90006,"请求/api/aemro/outpElement/getOutpElementByCondition 门诊病例接口失败"),
    Z_getOutpRecipe(90007,"请求/api/interface/medical/getOutpRecipe 门诊处方接口失败"),
    Z_gdownloadPage(90008,"下载图片出现异常！"),
    Z_getCheckReportByVisitNumberNew(90009,"请求/api/report/getList 通过visit_number 获取图片接口失败"),
    ;

    private int code;
    private String info;

}
