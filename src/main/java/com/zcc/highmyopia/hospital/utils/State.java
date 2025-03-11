package com.zcc.highmyopia.hospital.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName State
 * @Description
 * @Author aigao
 * @Date 2025/3/11 11:58
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum State {
    ONE_VISIT(0, "当天患者就诊信息下载失败"),
    SUCCESS_NOVisitData(-1,"当天没有visit数据，故下载结束"),
    SUCCESS(1,"下载完成"),
    ;
    private int state;
    private String info;
}
