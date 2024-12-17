package com.zcc.highmyopia.po.old;

import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author liangyue
 * @since 2021-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Caselist implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer pid;

    private String patientId;

    private String patientName;

    private Integer doctorId;

    @TableField("eyesightOD")
    private String eyesightOD;

    @TableField("eyesightOS")
    private String eyesightOS;

    @TableField("iOPOD")
    private Double iOPOD;

    @TableField("iOPOS")
    private Double iOPOS;

    @TableField("ALOD")
    private String ALOD;

    @TableField("ALOS")
    private String ALOS;

    private Integer result;

    @TableField("IOLMaster")
    private String IOLMaster;

    @TableField("retCAMOD")
    private String retCAMOD;

    @TableField("retCAMOS")
    private String retCAMOS;

    @TableField("retCAMresult")
    private String retCAMresult;

    @TableField("retCAMremarks")
    private String retCAMremarks;

    private String opt;

    private String optresult;

    /**
     * 光学相干断层成像
     */
    private String oct;

    private String octresult;

    private String octremarks;

    private LocalDate checktime;

    private Integer plan;

    private String diagnosis;

    private String recommend;

    private LocalDate visittime;

    private Integer stat;

    private Integer wanshan;

    private String onlyId;

    private Integer haveiol;

    private Integer haveoct;

    private Integer haveopt;
}
