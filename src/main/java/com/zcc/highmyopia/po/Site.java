package com.zcc.highmyopia.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("site")
public class Site implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 眼别ID

    @NotBlank(message = "眼别名称不能为空")
    private String siteName;  // 眼别名称

    private Integer status;  // 逻辑删除状态
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}