package com.zcc.highmyopia.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_detail")
public class OrderDetail implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 处方（医嘱）信息ID

    private Integer totalNumber;  // 总数量

    private String deptName;  // 科室名称

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cancelRefundDate;  // 退单日期

    private Long orderId;  // 订单ID

    private Long orderAttachId;  // 订单附件ID

    private Long itemCode;  // 项目编码

    private Integer eyeType;  // 眼别类型

    private String chmRecipeMethod;  // 中药处方方法

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime skinEnterTime;  // 皮肤给药时间

    private String specif;  // 规格

    private String frequency;  // 频率

    private Integer orderState;  // 订单状态

    private String decocType;  // 煎药类型

    private Integer oprLevel;  // 操作级别

    private BigDecimal totalDose;  // 总剂量

    private String recipeNumber;  // 处方编号

    private String itemName;  // 项目名称

    private String herbalAdjustName;  // 中药调整名称

    private Integer hospId;  // 医院ID

    private BigDecimal price;  // 价格

    private String chmNote;  // 中药备注

    private String execPlace;  // 执行地点

    private String applyNumber;  // 申请单编号

    private String batchNumber;  // 批号

    private Integer packingUnit;  // 包装单位

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifyDate;  // 修改日期

    private String addExecDeptName;  // 附加执行科室名称

    private String execDeptName;  // 执行科室名称

    private Integer outsourceSign;  // 外包标识

    private Integer deptId;  // 科室ID

    private String addExecDept;  // 附加执行科室

    private String herbalRequest;  // 中药请求

    private Integer everyNumber;  // 每次数量

    private Integer modifer;  // 修改人ID

    private String herbalUseName;  // 中药使用名称

    private String recipeName;  // 处方名称

    private String recipeKindName;  // 处方种类名称

    private Integer insureRange;  // 保险范围

    private String frequencyName;  // 频率名称

    private Integer orders;  // 订单数

    private String combiNumber;  // 组合编号

    private Long tempId;  // 临时ID

    private Integer herbalNumber;  // 中药数量

    private String refundReasonsName;  // 退单原因名称

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refundDate;  // 退单日期

    private Integer printTimes;  // 打印次数

    private Long skinEnterId;  // 皮肤给药ID

    private String adminRouteName;  // 给药途径名称

    private String longRange;  // 长期范围

    private String skinEnterName;  // 皮肤给药名称

    private String packingUnitName;  // 包装单位名称

    private String dripSpeed;  // 滴速

    private Long cancelRefunderId;  // 退单人ID

    private Long orderTemplId;  // 订单模板ID

    private Integer combiSeq;  // 组合顺序

    private Integer recipeKind;  // 处方种类

    private String orderTemplName;  // 订单模板名称

    private String eyeTypeName;  // 眼别类型名称

    private Integer usableDays;  // 有效天数

    private String doctorName;  // 医生姓名

    private Integer doctorId;  // 医生ID

    private String decocTypeName;  // 煎药类型名称

    private Integer execDept;  // 执行科室

    private String herbalAdjust;  // 中药调整

    private String cancelRefunderName;  // 退单人名称

    private Long tOutpOrderId;  // 门诊订单ID

    private String refundReasonsCode;  // 退单原因编码

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;  // 创建日期

    private String dosageUnitName;  // 剂量单位名称

    private String skinTestResult;  // 皮肤测试结果

    private String dosageUnit;  // 剂量单位

    private String anesthesiaModeName;  // 麻醉方式名称

    private String herbalUse;  // 中药使用

    private Integer creator;  // 创建人ID

    private Integer chronicDisease;  // 慢性病标识

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime billingTime;  // 开账时间

    private BigDecimal singleDose;  // 单次剂量

    private String orderTypeName;  // 订单类型名称

    private String doseUnitName;  // 剂量单位名称

    private Integer adminRoute;  // 给药途径

    private String orderRemark;  // 订单备注

    private Long execOrganId;  // 执行机构ID

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime execTime;  // 执行时间

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime  createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime  updateTime;
}