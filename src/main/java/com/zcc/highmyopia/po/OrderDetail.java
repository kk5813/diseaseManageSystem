package com.zcc.highmyopia.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_detail")
public class OrderDetail implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 处方（医嘱）信息ID

    private Integer totalNumber;  // 总数量
    private String deptName;  // 科室名称
    private Long orderId;  // 订单ID
    private Long itemCode;  // 项目编码
    private String itemName;  // 项目名称

    private BigDecimal price;  // 价格
    private String execPlace;  // 执行地点
    private String applyNumber;  // 申请单编号

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDate;  // 修改日期
}