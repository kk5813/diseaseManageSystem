CREATE TABLE IF NOT EXISTS `model_disease` (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '所配的AI诊断的信息',
    `name` VARCHAR(255) NOT NULL COMMENT '诊断疾病的名称',
    'desc' VARCHAR(255) COMMENT '所需诊断疾病说明',
    input VARCHAR(255) NOT NULL COMMENT '诊断所需的输入数据格式(item_name1,item_name2)',
    start_node INT NOT NULL COMMENT '疾病诊断流程起点模型主键',
    `create_time`  datetime COMMENT '创建时间',
    `update_time`  datetime COMMENT '更新时间'
);

CREATE TABLE IF NOT EXISTS `model_node` (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '模型的主键',
    disease_id INT NOT NULL COMMENT '所属疾病的ID',
    input VARCHAR(255) NOT NULL COMMENT '模型的输入数据格式',
    name VARCHAR(255) NOT NULL COMMENT '模型名称',
    `desc` VARCHAR(255) COMMENT '模型功能性描述',
    api VARCHAR(255) NOT NULL COMMENT '服务的api地址',
    `create_time`  datetime COMMENT '创建时间',
    `update_time`  datetime COMMENT '更新时间'
);

CREATE TABLE IF NOT EXISTS `model_line` (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '模型服务决策的主键',
    disease_id INT NOT NULL COMMENT '所属疾病的ID',
    model_from VARCHAR(255) NOT NULL COMMENT '根节点',
    model_to VARCHAR(255) NOT NULL COMMENT '子节点',
    limit_value VARCHAR(255) NOT NULL COMMENT '放行规则',
    `desc` VARCHAR(255) COMMENT '放行规则描述',
    `create_time`  datetime COMMENT '创建时间',
    `update_time`  datetime COMMENT '更新时间'
);