-- 创建新的数据库
CREATE DATABASE IF NOT EXISTS hospital_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hospital_db;

-- 1. 患者信息表
CREATE TABLE patients
(
    id        BIGINT NOT NULL PRIMARY KEY COMMENT '患者个人ID',
    `name`    VARCHAR(50) COMMENT '患者姓名',
    sex       TINYINT COMMENT '性别标识，1表示男性',
    sex_name  VARCHAR(6) COMMENT '性别名称，"男"或"女"',
    birthday  DATETIME COMMENT '患者出生日期，格式为 "YYYY-MM-DD HH:MM:SS"',
    id_number VARCHAR(18) UNIQUE COMMENT '身份证号',
    phone     VARCHAR(20) DEFAULT NULL COMMENT '电话号码',
    `status`  TINYINT      DEFAULT 1 COMMENT '逻辑删除: 0：不存在，1：存在'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


-- 2. 就诊信息表
CREATE TABLE visits
(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '自增ID',
    patient_id   BIGINT COMMENT '患者ID',
    doctor_id    BIGINT COMMENT '医生ID',
    dept_id      BIGINT COMMENT '科室ID',
    site_id      BIGINT COMMENT '眼别ID',
    visit_number VARCHAR(50) COMMENT '就诊号',
    diag_time    DATETIME COMMENT '诊断时间，格式为 "YYYY-MM-DD HH:MM:SS"',
    diag_order   INT COMMENT '诊断序号',
    diag_name    VARCHAR(255) COMMENT '诊断名称',
    diag_code    VARCHAR(50) COMMENT '诊断编码'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 3. 处方信息表
CREATE TABLE recipe
(
    id              BIGINT NOT NULL PRIMARY KEY COMMENT '处方ID',
    doctor_id       BIGINT COMMENT '医生ID',
    patient_id      BIGINT COMMENT '患者ID',
    dept_id         BIGINT COMMENT '科室ID',
    reg_number      VARCHAR(50) COMMENT '挂号编号',
    recipe_number   VARCHAR(50) UNIQUE COMMENT '处方编号',
    recipe_type     INT COMMENT '处方类型',
    billing_time    DATETIME COMMENT '开方时间格式为 "YYYY-MM-DD HH:MM:SS"',
    order_detail_id BIGINT COMMENT '处方（医嘱）信息ID'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 4. 处方医嘱信息
CREATE TABLE order_detail
(
    id                 BIGINT NOT NULL PRIMARY KEY COMMENT '处方（医嘱）信息ID',
    totalNumber        INT COMMENT '总数量',
    deptName           VARCHAR(50) COMMENT '科室名称',
    cancelRefundDate   DATETIME       DEFAULT NULL COMMENT '退单日期',
    orderId            BIGINT COMMENT '订单ID',
    orderAttachId      BIGINT         DEFAULT NULL COMMENT '订单附件ID',
    itemCode           BIGINT COMMENT '项目编码',
    eyeType            INT COMMENT '眼别类型',
    chmRecipeMethod    VARCHAR(255)   DEFAULT NULL COMMENT '中药处方方法',
    skinEnterTime      DATETIME       DEFAULT NULL COMMENT '皮肤给药时间',
    specif             VARCHAR(100)   DEFAULT NULL COMMENT '规格',
    frequency          VARCHAR(100)   DEFAULT NULL COMMENT '频率',
    orderState         INT COMMENT '订单状态',
    decocType          VARCHAR(100)   DEFAULT NULL COMMENT '煎药类型',
    oprLevel           INT            DEFAULT NULL COMMENT '操作级别',
    totalDose          DECIMAL(10, 2) DEFAULT NULL COMMENT '总剂量',
    recipeNumber       VARCHAR(50) COMMENT '处方编号',
    itemName           VARCHAR(255) COMMENT '项目名称',
    herbalAdjustName   VARCHAR(255)   DEFAULT NULL COMMENT '中药调整名称',
    hospId             INT COMMENT '医院ID',
    price              DECIMAL(10, 2) DEFAULT NULL COMMENT '价格',
    chmNote            TEXT           DEFAULT NULL COMMENT '中药备注',
    execPlace          VARCHAR(255) COMMENT '执行地点',
    applyNumber        VARCHAR(50) COMMENT '申请单编号',
    batchNumber        VARCHAR(50)    DEFAULT NULL COMMENT '批号',
    packingUnit        INT COMMENT '包装单位',
    modifyDate         DATETIME       DEFAULT NULL COMMENT '修改日期',
    addExecDeptName    VARCHAR(255)   DEFAULT NULL COMMENT '附加执行科室名称',
    execDeptName       VARCHAR(255) COMMENT '执行科室名称',
    outsourceSign      INT COMMENT '外包标识',
    deptId             INT COMMENT '科室ID',
    addExecDept        VARCHAR(255)   DEFAULT NULL COMMENT '附加执行科室',
    herbalRequest      VARCHAR(255)   DEFAULT NULL COMMENT '中药请求',
    everyNumber        INT COMMENT '每次数量',
    modifer            INT COMMENT '修改人ID',
    herbalUseName      VARCHAR(255)   DEFAULT NULL COMMENT '中药使用名称',
    recipeName         VARCHAR(255)   DEFAULT NULL COMMENT '处方名称',
    recipeKindName     VARCHAR(255)   DEFAULT NULL COMMENT '处方种类名称',
    insureRange        INT COMMENT '保险范围',
    frequencyName      VARCHAR(255)   DEFAULT NULL COMMENT '频率名称',
    orders             INT COMMENT '订单数',
    combiNumber        VARCHAR(50)    DEFAULT NULL COMMENT '组合编号',
    tempId             BIGINT COMMENT '临时ID',
    herbalNumber       INT COMMENT '中药数量',
    refundReasonsName  VARCHAR(255)   DEFAULT NULL COMMENT '退单原因名称',
    refundDate         DATETIME       DEFAULT NULL COMMENT '退单日期',
    printTimes         INT COMMENT '打印次数',
    skinEnterId        BIGINT         DEFAULT NULL COMMENT '皮肤给药ID',
    adminRouteName     VARCHAR(255)   DEFAULT NULL COMMENT '给药途径名称',
    longRange          VARCHAR(255)   DEFAULT NULL COMMENT '长期范围',
    skinEnterName      VARCHAR(255)   DEFAULT NULL COMMENT '皮肤给药名称',
    packingUnitName    VARCHAR(50)    DEFAULT NULL COMMENT '包装单位名称',
    dripSpeed          VARCHAR(255)   DEFAULT NULL COMMENT '滴速',
    cancelRefunderId   BIGINT         DEFAULT NULL COMMENT '退单人ID',
    orderTemplId       BIGINT         DEFAULT NULL COMMENT '订单模板ID',
    combiSeq           INT            DEFAULT NULL COMMENT '组合顺序',
    recipeKind         INT COMMENT '处方种类',
    orderTemplName     VARCHAR(255)   DEFAULT NULL COMMENT '订单模板名称',
    eyeTypeName        VARCHAR(50)    DEFAULT NULL COMMENT '眼别类型名称',
    usableDays         INT            DEFAULT NULL COMMENT '有效天数',
    doctorName         VARCHAR(255) COMMENT '医生姓名',
    doctorId           INT COMMENT '医生ID',
    decocTypeName      VARCHAR(255)   DEFAULT NULL COMMENT '煎药类型名称',
    execDept           INT COMMENT '执行科室',
    herbalAdjust       VARCHAR(255)   DEFAULT NULL COMMENT '中药调整',
    cancelRefunderName VARCHAR(255)   DEFAULT NULL COMMENT '退单人名称',
    tOutpOrderId       BIGINT COMMENT '门诊订单ID',
    refundReasonsCode  VARCHAR(50)    DEFAULT NULL COMMENT '退单原因编码',
    createDate         DATETIME COMMENT '创建日期',
    dosageUnitName     VARCHAR(50)    DEFAULT NULL COMMENT '剂量单位名称',
    skinTestResult     VARCHAR(255)   DEFAULT NULL COMMENT '皮肤测试结果',
    dosageUnit         VARCHAR(50)    DEFAULT NULL COMMENT '剂量单位',
    anesthesiaModeName VARCHAR(255)   DEFAULT NULL COMMENT '麻醉方式名称',
    herbalUse          VARCHAR(255)   DEFAULT NULL COMMENT '中药使用',
    creator            INT COMMENT '创建人ID',
    chronicDisease     INT            DEFAULT NULL COMMENT '慢性病标识',
    billingTime        DATETIME COMMENT '开账时间',
    singleDose         DECIMAL(10, 2) DEFAULT NULL COMMENT '单次剂量',
    refunder           INT            DEFAULT NULL COMMENT '退款人ID',
    skinTest           VARCHAR(255)   DEFAULT NULL COMMENT '皮肤测试',
    adminRoute         VARCHAR(255)   DEFAULT NULL COMMENT '给药途径',
    anesthesiaMode     VARCHAR(255)   DEFAULT NULL COMMENT '麻醉方式',
    medicDays          INT            DEFAULT NULL COMMENT '药品天数',
    herbalRequestName  VARCHAR(255)   DEFAULT NULL COMMENT '中药请求名称',
    execCombiSeq       INT            DEFAULT NULL COMMENT '执行组合顺序',
    dripSpeedName      VARCHAR(255)   DEFAULT NULL COMMENT '滴速名称',
    remarks            TEXT           DEFAULT NULL COMMENT '备注'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 5. 检验结果表
CREATE TABLE check_results
(
    id                   BIGINT NOT NULL PRIMARY KEY COMMENT '检验结果ID',
    patient_id           BIGINT COMMENT '患者ID',
    is_urgent            TINYINT COMMENT '0表示非紧急，1表示紧急',
    lab_item_name        VARCHAR(50) COMMENT '检验项目名称',
    report_name          VARCHAR(50) COMMENT '报告名称',
    lab_item_code        VARCHAR(50) COMMENT '检验项目代码',
    ref_range            VARCHAR(50) COMMENT '参考范围',
    lab_result_sign_name VARCHAR(50) COMMENT '检验结果标识',
    lab_final_value      VARCHAR(50) COMMENT '检验结果',
    visiting_no          VARCHAR(50) COMMENT '就诊编号',
    lab_result_unit_name VARCHAR(50) COMMENT '检验结果单位名称',
    audit_date           DATETIME COMMENT '审核日期，格式为 "YYYY-MM-DD HH:MM:SS"'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 6. 检查报告表
CREATE TABLE check_reports
(
    id           BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '报告表ID',
    patient_id   BIGINT COMMENT '患者ID',
    item_code    VARCHAR(50) COMMENT '检查项目编码',
    item_name    VARCHAR(50) COMMENT '检查项目名称',
    check_time   DATETIME COMMENT '检查时间',
    visit_number VARCHAR(50) COMMENT '就诊编号'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 7. 检查报告文件表
CREATE TABLE report_files
(
    id        BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '文件ID',
    report_id BIGINT COMMENT '检查ID',
    file_type VARCHAR(50) COMMENT '文件类型',
    file_url  TEXT COMMENT '文件下载URL'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 8. 病历表
CREATE TABLE element
(
    id              BIGINT NOT NULL PRIMARY KEY COMMENT '病历ID',
    patient_id      BIGINT COMMENT '患者ID',
    main_appeal     VARCHAR(255) COMMENT '主诉',
    past_history    VARCHAR(255) COMMENT '既往史',
    present_illness VARCHAR(255) COMMENT '现在史',
    allergy         VARCHAR(255) COMMENT '过敏史',
    special_os      VARCHAR(255) COMMENT '左眼科专科检查',
    special_od      VARCHAR(255) COMMENT '右眼科专科检查',
    visit_number    VARCHAR(50) COMMENT '就诊编号',
    physical_exam   VARCHAR(255) COMMENT '体格检查',
    dispose         VARCHAR(255) COMMENT '处理意见'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 9. 医生信息表
CREATE TABLE doctor
(
    id          BIGINT NOT NULL PRIMARY KEY COMMENT '医生ID',
    doctor_name VARCHAR(50) COMMENT '医生姓名',
    is_delete   TINYINT COMMENT '逻辑删除',
    `status`    TINYINT DEFAULT 1 COMMENT '逻辑删除: 0：不存在，1：存在'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 10. 科室信息表
CREATE TABLE dept
(
    id        BIGINT NOT NULL PRIMARY KEY COMMENT '科室ID',
    dept_name VARCHAR(50) COMMENT '科室名称',
    `status`  TINYINT DEFAULT 1 COMMENT '逻辑删除: 0：不存在，1：存在'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- 11. 眼别信息表
CREATE TABLE site
(
    id        BIGINT NOT NULL PRIMARY KEY COMMENT '眼别ID',
    site_name VARCHAR(50) COMMENT '眼别名称',
    `status`  TINYINT DEFAULT 1 COMMENT '逻辑删除: 0：不存在，1：存在'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;