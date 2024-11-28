### 1. `Patient` Class

```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("patients")
public class Patient implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 患者ID

    @NotBlank(message = "患者姓名不能为空")
    private String name;  // 患者姓名

    private Integer sex;  // 性别标识
    private String sexName;  // 性别名称

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime birthday;  // 出生日期

    private String idNumber;  // 身份证号
    private String phone;  // 电话号码

    private Integer status;  // 逻辑删除状态
}
```

### 2. `Doctor` Class

```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("doctor")
public class Doctor implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 医生ID

    @NotBlank(message = "医生姓名不能为空")
    private String doctorName;  // 医生姓名

    private Integer isDelete;  // 逻辑删除标识
    private Integer status;  // 逻辑删除状态
}
```

### 3. `Department` Class

```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dept")
public class Department implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 科室ID

    @NotBlank(message = "科室名称不能为空")
    private String deptName;  // 科室名称

    private Integer status;  // 逻辑删除状态
}
```

### 4. `Site` Class

```java
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
}
```

### 5. `Visit` Class

```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("visits")
public class Visit implements Serializable {
    
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 就诊号

    private Long patientId;  // 患者ID
    private Long doctorId;  // 医生ID
    private Long deptId;  // 科室ID
    private Long siteId;  // 眼别ID

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime diagTime;  // 诊断时间

    private Integer diagOrder;  // 诊断序号
    private String diagName;  // 诊断名称
    private String diagCode;  // 诊断编码
}
```

### 6. `Prescription` Class

```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("prescriptions")
public class Prescription implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 处方ID

    private Long doctorId;  // 医生ID
    private Long patientId;  // 患者ID
    private Long visitId;  // 就诊号
    private Long deptId;  // 科室ID

    private String regNumber;  // 挂号编号
    private String recipeNumber;  // 处方编号
    private Integer recipeType;  // 处方类型

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime billingTime;  // 开方时间
}
```

### 7. `OrderDetail` Class

```java
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
```

### 8. `LabResult` Class

```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("lab_results")
public class LabResult implements Serializable {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;  // 检验结果ID

    private Long patientId;  // 患者ID
    private Long visitId;  // 就诊号
    private String labItemName;  // 检验项目名称
    private String labItemCode;  // 检验项目代码

    private String refRange;  // 参考范围
    private String labFinalValue;  // 检验结果
    private String visitingNo;  // 就诊编号

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditDate;  // 审核日期
}
```

### 9. `CheckReport` Class

```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("check_reports")
public class CheckReport implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;  // 报告表ID

    private String studyUid;  // 检查ID（UID）
    private Long patientId;  // 患者ID
    private Long visitId;  // 就诊ID

    private String itemCode;  // 检查项目编码
    private String itemName;  // 检查项目名称

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkTime;  // 检查时间
}
```

### 10. `ReportFile` Class

```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("report_files")
public class ReportFile implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;  // 文件ID

    private Long reportId;  // 检查ID
    private String fileType;  // 文件类型
    private String fileUrl;  // 文件下载URL
}
```