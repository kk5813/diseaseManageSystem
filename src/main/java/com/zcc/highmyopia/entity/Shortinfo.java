package com.zcc.highmyopia.entity;

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
 * @since 2021-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Shortinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String patientId;

    private Integer doctorId;

    @TableField("eyesightOD")
    private String eyesightOD;

    @TableField("eyesightOS")
    private String eyesightOS;

    private String diagnosis;

    private Boolean hereditary;



}
