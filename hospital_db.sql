/*
 Navicat Premium Dump SQL

 Source Server         : 9344
 Source Server Type    : MySQL
 Source Server Version : 50729 (5.7.29-0ubuntu0.18.04.1)
 Source Host           : 43.136.178.202:3306
 Source Schema         : hospital_db

 Target Server Type    : MySQL
 Target Server Version : 50729 (5.7.29-0ubuntu0.18.04.1)
 File Encoding         : 65001

 Date: 01/01/2025 21:49:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for AI_model_result
-- ----------------------------
DROP TABLE IF EXISTS `AI_model_result`;
CREATE TABLE `AI_model_result`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模型结果ID',
  `patient_id` bigint(20) NULL DEFAULT NULL COMMENT '患者ID',
  `visit_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '就诊编号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '操作者用户ID',
  `diagnosis_process_id` bigint(20) NULL DEFAULT NULL COMMENT '诊断疾病流程ID',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '诊断结果描述信息',
  `urls` json NULL COMMENT '存储多个分割或检测结果图URL的JSON数组',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `AI_model_result_idx_patient_id`(`patient_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for check_reports
-- ----------------------------
DROP TABLE IF EXISTS `check_reports`;
CREATE TABLE `check_reports`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '报告表ID',
  `patient_id` bigint(20) NULL DEFAULT NULL COMMENT '患者ID',
  `item_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检查项目编码',
  `item_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检查项目名称',
  `check_time` datetime NULL DEFAULT NULL COMMENT '检查时间',
  `visit_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '就诊编号',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1612 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for check_results
-- ----------------------------
DROP TABLE IF EXISTS `check_results`;
CREATE TABLE `check_results`  (
  `id` bigint(20) NOT NULL COMMENT '检验结果ID',
  `patient_id` bigint(20) NULL DEFAULT NULL COMMENT '患者ID',
  `is_urgent` tinyint(4) NULL DEFAULT NULL COMMENT '0表示非紧急，1表示紧急',
  `lab_item_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检验项目名称',
  `report_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报告名称',
  `lab_item_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检验项目代码',
  `ref_range` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参考范围',
  `lab_result_sign_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检验结果标识',
  `lab_final_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检验结果',
  `visiting_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '就诊编号',
  `lab_result_unit_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '检验结果单位名称',
  `audit_date` datetime NULL DEFAULT NULL COMMENT '审核日期，格式为 \"YYYY-MM-DD HH:MM:SS\"',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept`  (
  `id` bigint(20) NOT NULL COMMENT '科室ID',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '科室名称',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '逻辑删除: 0：不存在，1：存在',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for doctor
-- ----------------------------
DROP TABLE IF EXISTS `doctor`;
CREATE TABLE `doctor`  (
  `id` bigint(20) NOT NULL COMMENT '医生ID',
  `doctor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '医生姓名',
  `site_id` bigint(20) NULL DEFAULT NULL COMMENT '科室ID',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '逻辑删除: 0：不存在，1：存在',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for element
-- ----------------------------
DROP TABLE IF EXISTS `element`;
CREATE TABLE `element`  (
  `id` bigint(20) NOT NULL COMMENT '病历ID',
  `patient_id` bigint(20) NULL DEFAULT NULL COMMENT '患者ID',
  `main_appeal` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主诉',
  `past_history` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '既往史',
  `present_illness` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '现在史',
  `allergy` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '过敏史',
  `special_os` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '左眼科专科检查',
  `special_od` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '右眼科专科检查',
  `visit_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '就诊编号',
  `physical_exam` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '体格检查',
  `dispose` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理意见',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for element_vision
-- ----------------------------
DROP TABLE IF EXISTS `element_vision`;
CREATE TABLE `element_vision`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `visit_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '就诊号',
  `patient_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '患者ID',
  `scd_os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '左眼裸眼视力',
  `scd_od` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '右眼裸眼视力',
  `scd_os_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '左眼裸眼视力值',
  `scd_od_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '右眼裸眼视力值',
  `ccd_os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '左眼矫正视力',
  `ccd_od` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '右眼矫正视力',
  `ccd_os_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '左眼矫正视力值',
  `ccd_od_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '右眼矫正视力值',
  `iop_os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '左眼眼压',
  `iop_od` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '右眼眼压',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for followup
-- ----------------------------
DROP TABLE IF EXISTS `followup`;
CREATE TABLE `followup`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `patient_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '病人id',
  `doctor_id` bigint(20) NULL DEFAULT NULL COMMENT '医生ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '科室ID',
  `visit_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '就诊号',
  `plan_visit_date` datetime NULL DEFAULT NULL COMMENT '计划随访时间',
  `visit_plan` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '随访计划安排',
  `visit_result` smallint(6) NOT NULL DEFAULT 0 COMMENT '随访结果，1随访过，0未随访，-1删除',
  `visit_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '随访内容',
  `visit_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '随访备注说明',
  `visit_date` datetime NULL DEFAULT NULL COMMENT '病人来访时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_followup_patient_id`(`patient_id`) USING BTREE,
  INDEX `idx_followup_plan_visit_date`(`plan_visit_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 110 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for model_disease
-- ----------------------------
DROP TABLE IF EXISTS `model_disease`;
CREATE TABLE `model_disease`  (
  `id` int(11) NOT NULL COMMENT '所配的AI诊断的信息',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '诊断疾病的名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所需诊断疾病说明',
  `input` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '诊断所需的输入数据格式(item_name1,item_name2)',
  `start_node` int(11) NOT NULL COMMENT '疾病诊断流程起点模型主键',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for model_line
-- ----------------------------
DROP TABLE IF EXISTS `model_line`;
CREATE TABLE `model_line`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '模型服务决策的主键',
  `disease_id` int(11) NOT NULL COMMENT '所属疾病的ID',
  `model_from` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '根节点',
  `model_to` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '子节点',
  `limit_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '放行规则',
  `use_output_url` bigint(5) NOT NULL COMMENT '是否使用之前的图片',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '放行规则描述',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 50 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for model_node
-- ----------------------------
DROP TABLE IF EXISTS `model_node`;
CREATE TABLE `model_node`  (
  `id` int(11) NOT NULL COMMENT '模型的主键',
  `disease_id` int(11) NOT NULL COMMENT '所属疾病的ID',
  `input` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型的输入数据格式',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模型功能性描述',
  `api` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务的api地址',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail`  (
  `id` bigint(20) NOT NULL COMMENT '处方（医嘱）信息ID',
  `total_number` int(11) NULL DEFAULT NULL COMMENT '总数量',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '科室名称',
  `cancel_refund_date` datetime NULL DEFAULT NULL COMMENT '退单日期',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT '订单ID',
  `order_attach_id` bigint(20) NULL DEFAULT NULL COMMENT '订单附件ID',
  `item_code` bigint(20) NULL DEFAULT NULL COMMENT '项目编码',
  `eye_type` int(11) NULL DEFAULT NULL COMMENT '眼别类型',
  `chm_recipe_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中药处方方法',
  `skin_enter_time` datetime NULL DEFAULT NULL COMMENT '皮肤给药时间',
  `specif` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '规格',
  `frequency` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '频率',
  `order_state` int(11) NULL DEFAULT NULL COMMENT '订单状态',
  `decoc_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '煎药类型',
  `opr_level` int(11) NULL DEFAULT NULL COMMENT '操作级别',
  `total_dose` decimal(10, 2) NULL DEFAULT NULL COMMENT '总剂量',
  `recipe_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处方编号',
  `item_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `herbal_adjust_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中药调整名称',
  `hosp_id` int(11) NULL DEFAULT NULL COMMENT '医院ID',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `chm_note` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '中药备注',
  `exec_place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行地点',
  `apply_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请单编号',
  `batch_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '批号',
  `packing_unit` int(11) NULL DEFAULT NULL COMMENT '包装单位',
  `modify_date` datetime NULL DEFAULT NULL COMMENT '修改日期',
  `add_exec_dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附加执行科室名称',
  `exec_dept_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行科室名称',
  `outsource_sign` int(11) NULL DEFAULT NULL COMMENT '外包标识',
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '科室ID',
  `add_exec_dept` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附加执行科室',
  `herbal_request` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中药请求',
  `every_number` int(11) NULL DEFAULT NULL COMMENT '每次数量',
  `modifer` int(11) NULL DEFAULT NULL COMMENT '修改人ID',
  `herbal_use_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中药使用名称',
  `recipe_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处方名称',
  `recipe_kind_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处方种类名称',
  `insure_range` int(11) NULL DEFAULT NULL COMMENT '保险范围',
  `frequency_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '频率名称',
  `orders` int(11) NULL DEFAULT NULL COMMENT '订单数',
  `combi_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组合编号',
  `temp_id` bigint(20) NULL DEFAULT NULL COMMENT '临时ID',
  `herbal_number` int(11) NULL DEFAULT NULL COMMENT '中药数量',
  `refund_reasons_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退单原因名称',
  `refund_date` datetime NULL DEFAULT NULL COMMENT '退单日期',
  `print_times` int(11) NULL DEFAULT NULL COMMENT '打印次数',
  `skin_enter_id` bigint(20) NULL DEFAULT NULL COMMENT '皮肤给药ID',
  `admin_route_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '给药途径名称',
  `long_range` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '长期范围',
  `skin_enter_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '皮肤给药名称',
  `packing_unit_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '包装单位名称',
  `drip_speed` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '滴速',
  `cancel_refunder_id` bigint(20) NULL DEFAULT NULL COMMENT '退单人ID',
  `order_templ_id` bigint(20) NULL DEFAULT NULL COMMENT '订单模板ID',
  `combi_seq` int(11) NULL DEFAULT NULL COMMENT '组合顺序',
  `recipe_kind` int(11) NULL DEFAULT NULL COMMENT '处方种类',
  `order_templ_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单模板名称',
  `eye_type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '眼别类型名称',
  `usable_days` int(11) NULL DEFAULT NULL COMMENT '有效天数',
  `doctor_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '医生姓名',
  `doctor_id` int(11) NULL DEFAULT NULL COMMENT '医生ID',
  `decoc_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '煎药类型名称',
  `exec_dept` int(11) NULL DEFAULT NULL COMMENT '执行科室',
  `herbal_adjust` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中药调整',
  `cancel_refunder_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退单人名称',
  `t_outp_order_id` bigint(20) NULL DEFAULT NULL COMMENT '门诊订单ID',
  `refund_reasons_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '退单原因编码',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `dosage_unit_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '剂量单位名称',
  `skin_test_result` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '皮肤测试结果',
  `dosage_unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '剂量单位',
  `anesthesia_mode_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '麻醉方式名称',
  `herbal_use` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '中药使用',
  `creator` int(11) NULL DEFAULT NULL COMMENT '创建人ID',
  `chronic_disease` int(11) NULL DEFAULT NULL COMMENT '慢性病标识',
  `billing_time` datetime NULL DEFAULT NULL COMMENT '开账时间',
  `single_dose` decimal(10, 2) NULL DEFAULT NULL COMMENT '单次剂量',
  `order_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单类型名称',
  `dose_unit_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '剂量单位名称',
  `admin_route` int(11) NULL DEFAULT NULL COMMENT '给药途径',
  `order_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `exec_organ_id` bigint(20) NULL DEFAULT NULL COMMENT '执行机构ID',
  `exec_time` datetime NULL DEFAULT NULL COMMENT '执行时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单详细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for patients
-- ----------------------------
DROP TABLE IF EXISTS `patients`;
CREATE TABLE `patients`  (
  `id` bigint(20) NOT NULL COMMENT '患者个人ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '患者姓名',
  `sex` tinyint(4) NULL DEFAULT NULL COMMENT '性别标识，1表示男性',
  `sex_name` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别名称，\"男\"或\"女\"',
  `birthday` datetime NULL DEFAULT NULL COMMENT '患者出生日期，格式为 \"YYYY-MM-DD HH:MM:SS\"',
  `id_number` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '逻辑删除: 0：不存在，1：存在',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_number`(`id_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for recipe
-- ----------------------------
DROP TABLE IF EXISTS `recipe`;
CREATE TABLE `recipe`  (
  `id` bigint(20) NOT NULL COMMENT '处方ID',
  `doctor_id` bigint(20) NULL DEFAULT NULL COMMENT '医生ID',
  `patient_id` bigint(20) NULL DEFAULT NULL COMMENT '患者ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '科室ID',
  `reg_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '挂号编号',
  `recipe_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处方编号',
  `recipe_type` int(11) NULL DEFAULT NULL COMMENT '处方类型',
  `billing_time` datetime NULL DEFAULT NULL COMMENT '开方时间格式为 \"YYYY-MM-DD HH:MM:SS\"',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `recipe_number`(`recipe_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for report_files
-- ----------------------------
DROP TABLE IF EXISTS `report_files`;
CREATE TABLE `report_files`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文件ID',
  `report_id` bigint(20) NULL DEFAULT NULL COMMENT '检查ID',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文件下载URL',
  `is_down_load` tinyint(4) NULL DEFAULT NULL COMMENT '文件是否已经下载',
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '已经下载的图像保存的路径地址(绝对地址)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1873242614027780123 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for site
-- ----------------------------
DROP TABLE IF EXISTS `site`;
CREATE TABLE `site`  (
  `id` bigint(20) NOT NULL COMMENT '眼别ID',
  `site_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '眼别名称',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '逻辑删除: 0：不存在，1：存在',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_login_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `salt` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_status` int(11) NOT NULL COMMENT '用户状态：0-管理员，-1有效，1医生，2护士',
  `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `modifier` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `user_login_name`(`user_login_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1122 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for visits
-- ----------------------------
DROP TABLE IF EXISTS `visits`;
CREATE TABLE `visits`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `patient_id` bigint(20) NULL DEFAULT NULL COMMENT '患者ID',
  `doctor_id` bigint(20) NULL DEFAULT NULL COMMENT '医生ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '科室ID',
  `site_id` bigint(20) NULL DEFAULT NULL COMMENT '眼别ID',
  `visit_number` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '就诊号',
  `diag_time` datetime NULL DEFAULT NULL COMMENT '诊断时间，格式为 \"YYYY-MM-DD HH:MM:SS\"',
  `diag_order` int(11) NULL DEFAULT NULL COMMENT '诊断序号',
  `diag_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '诊断名称',
  `diag_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '诊断编码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47638 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 创建随访模板表，记录各科室的随访计划模板
-- Table structure for followup_template
-- ----------------------------
DROP TABLE IF EXISTS `followup_template`;
CREATE TABLE `followup_template` (
 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '模板ID，主键，自增' ,
 `template_name` VARCHAR(100) NOT NULL COMMENT '模板名称，唯一' ,
 `dept_id` BIGINT(20)  NOT NULL COMMENT '所属科室ID' ,
 `interval_value` INT NOT NULL DEFAULT 7  COMMENT '随访间隔值（整数）单位天' ,
 `visit_plan` VARCHAR(30) NOT NULL COMMENT '随访计划安排' ,
 `visit_result` smallint(6) NOT NULL DEFAULT 0 COMMENT '随访结果，1随访过，0未随访，-1删除',
 `visit_content` VARCHAR(255) NOT NULL COMMENT '随访内容描述' ,
 `visit_remark` VARCHAR(255) DEFAULT NULL COMMENT '备注说明，可选' ,
 `is_active` smallint(4) NOT NULL DEFAULT 1 COMMENT '激活状态：1=启用，0=停用' ,
 `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
 `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
 `creator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
 `modifier` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
 PRIMARY KEY (`id`) USING BTREE,
 UNIQUE KEY `uk_template_name` (`template_name`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC COMMENT='随访模板表：存储随访计划';


-- ----------------------------
-- View structure for followup_patient_view
-- ----------------------------
DROP VIEW IF EXISTS `followup_patient_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `followup_patient_view` AS
select `f`.`id` AS `followup_id`,`f`.`patient_id` AS `patient_id`, `f`.`doctor_id` AS `doctor_id`, `f`.`dept_id` AS `dept_id`, `f`.`visit_number` AS `visit_number`, `f`.`plan_visit_date` AS `plan_visit_date`,`f`.`visit_plan` AS `visit_plan`,`f`.`visit_result` AS `visit_result`,`f`.`visit_content` AS `visit_content`,`f`.`visit_remark` AS `visit_remark`,`f`.`visit_date` AS `visit_date`,`p`.`name` AS `patient_name`,`p`.`sex_name` AS `gender`,`p`.`phone` AS `telephone`,`p`.`id_number` AS `id_number`
from (`followup` `f` left join `patients` `p` on((`f`.`patient_id` = `p`.`id`)));

-- ----------------------------
-- View structure for patient_visit_summary_view
-- ----------------------------
DROP VIEW IF EXISTS `patient_visit_summary_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `patient_visit_summary_view` AS select `e`.`visit_number` AS `visit_number`,`e`.`id` AS `element_id`,`e`.`patient_id` AS `patient_id`,`e`.`main_appeal` AS `main_appeal`,`e`.`past_history` AS `past_history`,`e`.`present_illness` AS `present_illness`,`e`.`allergy` AS `allergy`,`e`.`special_os` AS `special_os`,`e`.`special_od` AS `special_od`,`e`.`physical_exam` AS `physical_exam`,`e`.`dispose` AS `dispose`,`v`.`diag_time` AS `diag_time`,`v`.`diag_name` AS `diag_name`,`v`.`diag_code` AS `diag_code`,`ev`.`scd_os` AS `scd_os`,`ev`.`scd_od` AS `scd_od`,`ev`.`scd_os_value` AS `scd_os_value`,`ev`.`scd_od_value` AS `scd_od_value`,`ev`.`ccd_os` AS `ccd_os`,`ev`.`ccd_od` AS `ccd_od`,`ev`.`ccd_os_value` AS `ccd_os_value`,`ev`.`ccd_od_value` AS `ccd_od_value`,`ev`.`iop_os` AS `iop_os`,`ev`.`iop_od` AS `iop_od` from ((`element` `e` left join `visits` `v` on((`e`.`visit_number` = `v`.`visit_number`))) left join `element_vision` `ev` on((`e`.`visit_number` = `ev`.`visit_number`)));

SET FOREIGN_KEY_CHECKS = 1;




