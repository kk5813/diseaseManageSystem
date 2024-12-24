### 1.1 患者就诊信息(测试成功)

#### 1.1.1 基本信息

> 请求host: http://sit.aierchina.com:8710/external-api
>
> 请求path：/api/interface/medical/getPatientVisit
>
> 请求方式：POST
>
> 接口描述：该接口用于查询患者基本信息和就诊信息


#### 1.1.2 请求参数

格式：application/json

参数说明：

| 参数代码  | 参数名称 | 参数类型 | 参数长度 | 代码标识 | 是否必填 | 说明 |
| --------- | -------- | -------- | -------- | -------- | -------- | ---- |
| DiagBdate | 开始日期 | 日期型   |          |          | Y        |      |
| DiagEdate | 结束日期 | 日期型   |          |          | Y        |      |

请求参数代码：

```java
	    String hospId ="1824";
        String appKey = "Cdm_1824";
        String appSecret="ywgp.lbol";

        Map<String,String> querys = new HashMap<>();
        querys.put("diagBdate","20240520");
        querys.put("diagEdate","20240529");	
        String reqJson ="";
        Map<String,String> headers =new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();

        Response resp = HttpClientUtils.httpPostJson(host, path, connectTimeout, headers, querys, reqJson,signHeaderPrefixList, appKey, appSecret, hospId);
```

#### 1.1.3 响应数据

参数格式：application/json

参数说明：

| 参数名称              | 类型   | 必填 | 描述                                       |
| :-------------------- | :----- | :--- | :----------------------------------------- |
| birthday(patients)    | string | 是   | 患者出生日期，格式为 "YYYY-MM-DD HH:MM:SS" |
| patientName(patients) | string | 是   | 患者姓名                                   |
| deptName(dept)        | string | 是   | 科室名称                                   |
| patientId(patients)   | long   | 是   | 患者ID                                     |
| diagTime(visits)      | string | 是   | 诊断时间，格式为 "YYYY-MM-DD HH:MM:SS"     |
| visitNumber(visits)   | string | 是   | 就诊号                                     |
| diagOrder(visits)     | int    | 是   | 诊断序号                                   |
| sex(patient)          | int    | 是   | 性别标识，1表示男性                        |
| deptId(visits)        | int    | 是   | 科室ID                                     |
| siteName(site)        | string | 是   | 眼别                                       |
| diagName(visits)      | string | 是   | 诊断名称                                   |
| diagCode(visits)      | string | 是   | 诊断编码                                   |
| doctorName(doctor)    | string | 是   | 医生姓名                                   |
| doctorId(visits)      | int    | 是   | 医生ID                                     |
| sexName(patients)     | string | 是   | 性别名称，"男"或"女"                       |
| siteId(visits)        | int    | 是   | 眼别ID                                     |

响应数据样例：

```json
{
    "data": [
        {
            "birthday": "1997-10-11 00:00:00",
            "patientName": "周印",
            "deptName": "白内障科",
            "patientId": 1529282661958848500,
            "diagTime": "2024-05-21 14:25:29",
            "visitNumber": "MZ202405210003",
            "diagOrder": 0,
            "sex": 1,
            "deptId": 100484,
            "siteName": "双眼",
            "diagName": "干眼综合征",
            "diagCode": "H04.103",
            "doctorName": "罗迁逊",
            "doctorId": 55367,
            "sexName": "男",
            "siteId": 3
        }
    ]
}
```



### 1.2 门诊处方信息(测试成功)

#### 1.2.1 基本信息

> 请求host: http://sit.aierchina.com:8710/external-api
>
> 请求path：/api/interface/medical/getOutpRecipe
>
> 请求方式：POST
>
> 接口描述：该接口用于查询患者处方信息


#### 1.2.2 请求参数

格式：application/json

参数说明：

| 参数代码     | 参数名称 | 参数类型 | 参数长度 | 代码标识 | 是否必填 | 说明 |
| ------------ | -------- | -------- | -------- | -------- | -------- | ---- |
| billingBdate | 开始日期 | 日期型   |          |          | Y        |      |
| billingEdate | 结束日期 | 日期型   |          |          | Y        |      |

请求参数代码：

```java

        String hospId ="1824";
        String appKey = "Cdm_1824";
        String appSecret="ywgp.lbol";

        Map<String,String> querys = new HashMap<>();
        querys.put("billingBdate","20240801");
        querys.put("billingEdate","20241125");
        String reqJson ="";
        Map<String,String>headers =new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpPostJson(host, path, connectTimeout, headers, querys, reqJson,signHeaderPrefixList,appKey,appSecret,hospId);
```

#### 1.2.3 响应数据

参数格式：application/json

参数说明：

| 字段名称                  | 类型   | 描述             | 备注                         |
| :------------------------ | :----- | :--------------- | :--------------------------- |
| patientName(patients)     | string | 患者姓名         |                              |
| deptName(dept)            | string | 科室名称         |                              |
| regNumber(recipe)         | string | 挂号编号         |                              |
| doctorName(doctor)        | string | 医生姓名         |                              |
| recipeNumber(recipe)      | string | 处方编号         |                              |
| recipeType(recipe)        | int    | 处方类型         |                              |
| patientId(recipe)         | long   | 患者ID           |                              |
| billingTime(recipe)       | string | 开方时间         | 格式为 "YYYY-MM-DD HH:MM:SS" |
| id(recipe)                | long   | 处方ID           |                              |
| orderdetail(order_detail) | array  | 处方（医嘱）信息 |                              |

响应数据样例：

```json
{
    "data": [
        {
            "patientName": "苹果",
            "deptName": "青白科",
            "regNumber": "MZ202411200002",
            "doctorName": "陈古敏",
            "recipeNumber": "CF202411200001",
            "recipeType": 1,
            "patientId": 1032932518382456800,
            "billingTime": "2024-11-20 14:03:26",
            "id": 1859115314996940800,
            "orderdetail": [
                {
                    "totalNumber": 1,
                    "deptName": "青白科",
                    "cancelRefundDate": null,
                    "orderId": 1859115314996940800,
                    "orderAttachId": null,
                    "itemCode": 1789171081824669700,
                    "eyeType": 1,
                    "chmRecipeMethod": null,
                    "skinEnterTime": null,
                    "specif": null,
                    "frequency": null,
                    "orderState": 3,
                    "decocType": null,
                    "oprLevel": null,
                    "totalDose": null,
                    "recipeNumber": "CF202411200001",
                    "itemName": "眼部B超",
                    "herbalAdjustName": null,
                    "hospId": 1824,
                    "price": null,
                    "id": 1859115320042688500,
                    "chmNote": null,
                    "execPlace": "18246",
                    "applyNumber": "JCSQ202411200001",
                    "batchNumber": null,
                    "packingUnit": 100,
                    "modifyDate": "2024-11-20 14:04:23",
                    "addExecDeptName": null,
                    "execDeptName": "医学影像科",
                    "outsourceSign": 0,
                    "deptId": 100484,
                    "addExecDept": null,
                    "herbalRequest": null,
                    "everyNumber": 1,
                    "modifer": 48937,
                    "herbalUseName": null,
                    "recipeName": null,
                    "recipeKindName": "特检",
                    "insureRange": 1,
                    "frequencyName": null,
                    "orders": 4,
                    "combiNumber": null,
                    "tempId": 1859115272567361500,
                    "herbalNumber": 0,
                    "refundReasonsName": null,
                    "refundDate": null,
                    "printTimes": 0,
                    "skinEnterId": null,
                    "adminRouteName": null,
                    "longRange": null,
                    "skinEnterName": null,
                    "packingUnitName": "次",
                    "dripSpeed": null,
                    "cancelRefunderId": null,
                    "orderTemplId": null,
                    "combiSeq": null,
                    "recipeKind": 7,
                    "orderTemplName": null,
                    "eyeTypeName": "左眼",
                    "usableDays": null,
                    "doctorName": "陈古敏",
                    "doctorId": 48937,
                    "decocTypeName": null,
                    "execDept": 103864,
                    "herbalAdjust": null,
                    "cancelRefunderName": null,
                    "tOutpOrderId": 1859115314996940800,
                    "refundReasonsCode": null,
                    "createDate": "2024-11-20 14:03:16",
                    "dosageUnitName": null,
                    "skinTestResult": null,
                    "dosageUnit": null,
                    "anesthesiaModeName": null,
                    "herbalUse": null,
                    "creator": 48937,
                    "chronicDisease": 0,
                    "billingTime": "2024-11-20 14:03:16",
                    "singleDose": null,
                    "refunder": null,
                    "skinTest": null,
                    "adminRoute": null,
                    "anesthesiaMode": null,
                    "medicDays": null,
                    "herbalRequestName": null,
                    "execCombiSeq": 1,
                    "dripSpeedName": null,
                    "remarks": null
                }
            ]
        },
    ],
    "resultCode": "1",
    "resultMsg": "操作成功！"
}
```



### 1.3 患者检验结果(测试成功)

#### 1.3.1 基本信息

> 请求host: http://sit.aierchina.com:8710/external-api
>
> 请求path：/alis/interface/reportDetail/getReportDetail
>
> 请求方式：POST
>
> 接口描述：该接口用于查询患者的检验结果


#### 1.3.2 请求参数

格式：application/json

参数说明：

| 参数代码       | 参数名称 | 参数类型 | 参数长度 | 代码标识 | 是否必填 |
| -------------- | -------- | -------- | -------- | -------- | -------- |
| auditDateBegin | 开始日期 | 日期型   |          |          | Y        |
| auditDateEnd   | 结束日期 | 日期型   |          |          | Y        |
| visitNumber    | 就诊号   | 字符型   | 20       |          |          |

请求参数代码：

```java
		int connectTimeout = 7200;
        String hospId = "1824";
        String appKey = "Cdm_1824";
        String appSecret = "ywgp.lbol";

        Map<String, String> r = new HashMap<>();
        r.put("auditDateBegin", "2024-08-01");
        r.put("auditDateEnd", "2024-11-15");
        //r.put("visitNumber", "1134234324");
        String reqJson = JsonUtil.toJson(r);

        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> querys = null;
        List<String> signHeaderPrefixList = new ArrayList<>();

        Response resp = HttpClientUtils.httpPostJson(host, path, connectTimeout, headers, querys, reqJson,signHeaderPrefixList, appKey, appSecret, hospId);
```

#### 1.3.3 响应数据

参数格式：application/json

参数说明：

|                                  |        |                                            |      |
| :------------------------------- | :----- | :----------------------------------------- | :--- |
| 字段名称                         | 类型   | 描述                                       | 备注 |
| patientName(patients)            | string | 患者姓名                                   |      |
| isUrgent(check_results)          | int    | 是否紧急，0表示非紧急，1表示紧急           |      |
| labItemName(check_results)       | string | 检验项目名称                               |      |
| reportName(check_results)        | string | 报告名称                                   |      |
| patientId(check_results)         | long   | 患者ID                                     |      |
| labItemCode(check_results)       | string | 检验项目代码                               |      |
| refRange(check_results)          | string | 参考范围                                   |      |
| labResultSignName(check_results) | string | 检验结果标识                               |      |
| labFinalValue(check_results)     | string | 检验结果                                   |      |
| visitingNo(check_results)        | string | 就诊编号                                   |      |
| sexName(patients)                | string | 性别名称，"男"或"女"                       |      |
| patientBrithday(patients)        | string | 患者出生日期，格式为 "YYYY-MM-DD HH:MM:SS" |      |
| labResultUnitName(check_results) | string | 检验结果单位名称                           |      |
| id(check_results)                | long   | 检验结果ID                                 |      |
| auditDate(check_results)         | string | 审核日期，格式为 "YYYY-MM-DD HH:MM:SS"     |      |

响应数据样例：

```json
{
    "data": [
        {
            "patientName": "杨奇韵",
            "isUrgent": 0,
            "labItemName": "乙肝E抗体",
            "reportName": "乙肝五项（定量分析法）+丙肝抗体发光法+HIV抗体检查+梅毒抗体发光法",
            "patientId": 1796786711460069400,
            "labItemCode": "Anti-HBe",
            "refRange": "0-0.5",
            "labResultSignName": null,
            "labFinalValue": "0.01",
            "visitingNo": "MZ202406010634",
            "sexName": "男",
            "patientBrithday": "1986-03-27 00:00:00",
            "labResultUnitName": "S/CO",
            "id": 1799240480341352400,
            "auditDate": "2024-08-08 10:54:18"
        },
        {
            "patientName": "杨奇韵",
            "isUrgent": 0,
            "labItemName": "乙肝表面抗体",
            "reportName": "乙肝五项（定量分析法）+丙肝抗体发光法+HIV抗体检查+梅毒抗体发光法",
            "patientId": 1796786711460069400,
            "labItemCode": "Anti-HBs",
            "refRange": "0-10",
            "labResultSignName": null,
            "labFinalValue": "6.33",
            "visitingNo": "MZ202406010634",
            "sexName": "男",
            "patientBrithday": "1986-03-27 00:00:00",
            "labResultUnitName": "mIU/mL",
            "id": 1799240480341352400,
            "auditDate": "2024-08-08 10:54:18"
        },
    ],
    "resultCode": "1",
    "resultMsg": "成功！"
}
```

### 1.4 患者检查结果

#### 1.4.1 基本信息

> 请求host: http://10.0.225.6:8083
>
> 请求path：/api/report/getList
>
> 请求方式：get
>
> 接口描述：该接口用于查询患者的检查结果和图片


#### 1.4.2 请求参数

格式：params

参数说明：

| 参数代码    | 参数名称 | 参数类型 | 参数长度 | 代码标识 | 是否必填 |
| ----------- | -------- | -------- | -------- | -------- | -------- |
| physc_bdate | 开始日期 | 日期型   |          |          | Y        |
| physc_edate | 结束日期 | 日期型   |          |          | Y        |
| patient_id  | 患者ID   | 字符型   | 20       |          |          |

请求参数代码：

```java
        String hospId ="1824";
        String appKey = "Cdm_1824";
        String appSecret="ywgp.lbol";

        Map<String,String> querys = new HashMap<>();
        querys.put("billingBdate","20240801");
        querys.put("billingEdate","20241125");
        String reqJson ="";
        Map<String,String>headers =new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpPostJson(host, path, connectTimeout, headers, querys, reqJson,signHeaderPrefixList,appKey,appSecret,hospId);
```

#### 1.4.3 响应数据

参数格式：application/json

参数说明：

| 字段名称 | 类型   | 描述             | 备注                                      |
| :------- | :----- | :--------------- | :---------------------------------------- |
| code     | string | 响应状态码       | "200"表示请求成功                         |
| msg      | string | 响应消息         | "success"表示请求成功，其他值表示错误信息 |
| error    | null   | object           | 错误对象                                  |
| data     | array  | 检查报告信息数组 | 包含多个检查报告的详细信息                |

**data 数组元素结构**

| 字段名称                    | 类型   | 描述             | 备注                         |
| :-------------------------- | :----- | :--------------- | :--------------------------- |
| item_code(check_reports)    | string | 检查项目编码     |                              |
| patient_id(check_reports)   | string | 患者ID           |                              |
| files(report_files)         | array  | 相关文件信息数组 | 包含文件类型和下载URL        |
| item_name(check_reports)    | string | 检查项目名称     |                              |
| <u>id</u>                   | string | 检查ID           | 通常为研究UID                |
| <u>patient_sid</u>          | null   | string           | 患者SID                      |
| visit_number(check_reports) | string | 就诊编号         |                              |
| check_time(check_reports)   | string | 检查时间         | 格式为 "YYYY-MM-DD HH:MM:SS" |

**files 数组元素结构**

| 字段名称 | 类型   | 描述        | 备注                                 |
| :------- | :----- | :---------- | :----------------------------------- |
| type     | string | 文件类型    | 如 "application/pdf" 或 "image/jpeg" |
| url      | string | 文件下载URL | 用于下载检查报告文件                 |

响应数据样例：

```json
{
    "code": "200",
    "msg": "success",
    "error": null,
    "data": [
        {
            "item_code": "310300064A",
            "patient_id": "1855597141015232514",
            "files": [
                {
                    "type": "application/pdf",
                    "url": "/api/report/file?requestType=WADO&studyUID=1.2.156.112817.238552294593395687244168015881286355446&seriesUID=1.2.156.112817.277988465913857827643990061803889468889&objectUID=1.2.156.112817.61777326525601769705670426710770458639"
                }
            ],
            "item_name": "光学相干断层成像（OCT）",
            "id": "1.2.156.112817.238552294593395687244168015881286355446",
            "patient_sid": null,
            "visit_number": "MZ202411120358",
            "check_time": "2024-11-12 17:21:29"
        },
        {
            "item_code": "310300064A",
            "patient_id": "1855597141015232514",
            "files": [
                {
                    "type": "image/jpeg",
                    "url": "/api/report/file?requestType=WADO&studyUID=1.2.156.112817.238552294593395687244168015881286355446&seriesUID=1.2.156.112817.997210318758019230121962460986222.2.16&objectUID=1.2.156.112817.997210318758019230121962460986222.1.16"
                }
            ],
            "item_name": "光学相干断层成像（OCT）",
            "id": "1.2.156.112817.238552294593395687244168015881286355446",
            "patient_sid": null,
            "visit_number": "MZ202411120358",
            "check_time": "2024-11-12 17:21:29"
        }
    ]
}
```



### 1.5 患者门诊病历(测试成功)

#### 1.5.1 基本信息

> 请求host: http://sit.aierchina.com:8707
>
> 请求path：/api/aemro/outpElement/getOutpElementByCondition
>
> 请求方式：POST
>
> 接口描述：该接口用于查询患者门诊病历


#### 1.5.2 请求参数

格式：application/json

参数说明：

| 参数代码     | 参数名称 | 参数类型 | 参数长度 | 代码标识 | 是否必填 |
| ------------ | -------- | -------- | -------- | -------- | -------- |
| aemr_bdate   | 开始日期 | 日期型   |          |          | Y        |
| aemr_edate   | 结束日期 | 日期型   |          |          | Y        |
| Visit_number | 就诊号   | 字符型   | 20       |          |          |

请求参数代码：

```java
        int connectTimeout=7200;
        String hospId = "1824";
        String appKey = "Cdm_1824";
        String appSecret = "ywgp.lbol";

        Map<String,String> querys = new HashMap<>();
        querys.put("aemr_bdate","2024-06-26");
        querys.put("aemr_edate","20240725");
        querys.put("Visit_number","MZ202407071064");
        String reqJson = JsonUtil.toJson(querys);
        Map<String,String>headers =new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpPostJson(host, path, connectTimeout, headers, querys, reqJson,signHeaderPrefixList,appKey,appSecret,hospId);
```

#### 1.5.3 响应数据

参数格式：application/json

参数说明：

| 字段名称                 | 类型   | 描述           |
| ------------------------ | :----- | :------------- |
| MAIN_APPEAL(element)     | string | 主诉           |
| PAST_HISTORY(element)    | string | 既往史         |
| PRESENT_ILLNESS(element) | string | 现病史         |
| ALLERGY(element)         | string | 过敏史         |
| SPECIAL_OS(element)      | string | 左眼科专科检查 |
| SPECIAL_OD(element)      | string | 右眼科专科检查 |
| Visit_number(element)    | string | 就诊编号       |
| patient_id(element)      | long   | 患者ID         |
| patient_name(patient)    | string | 患者姓名       |
| PHYSICAL_EXAM(element)   | string | 体格检查       |
| ID(element)              | string | 病历ID         |
| DISPOSE(element)         | string | 处理意见       |

响应数据样例：

```json
{
    "data": [
        {
            "MAIN_APPEAL": "左眼进异物5小时",
            "PAST_HISTORY": null,
            "PRESENT_ILLNESS": "左眼进异物5小时，出现眼痛，眼异物感，无视力下降",
            "ALLERGY": null,
            "SPECIAL_OS": null,
            "SPECIAL_OD": null,
            "Visit_number": "MZ202407071064",
            "patient_id": 1809970417345020000,
            "patient_name": "林柔汐",
            "PHYSICAL_EXAM": null,
            "ID": "1809971108367556610",
            "DISPOSE": "左氧氟沙星滴眼液 1.0滴 左眼 滴眼 每日三次,重组牛碱性成纤维细胞生长因子眼用凝胶 1.0滴 左眼 滴眼 每日四次；"
        }
    ],
    "resultCode": "1",
    "resultMsg": "成功！"
}
```



### 1.6 患者敏感信息（电话）查询(测试成功)

#### 1.6.1 基本信息

> 请求host: http://sit.aierchina.com:8710/external-api
>
> 请求path：/api/interface/patientInfo/getById
>
> 请求方式：POST
>
> 接口描述：该接口用于查询患者敏感信息（电话）


#### 1.6.2 请求参数

格式：application/json

参数说明：

| 参数代码 | 参数名称 | 参数类型 | 参数长度 | 代码标识 | 是否必填 |
| -------- | -------- | -------- | -------- | -------- | -------- |
| id       | 患者ID   | 25       |          |          | Y        |

请求参数代码：

```java
        String hospId ="1824";
        String appKey = "Cdm_1824";
        String appSecret="ywgp.lbol";


        Map<String, String> r = new HashMap<>();
        r.put("id", "1796786711460069377");
        String reqJson = JsonUtil.toJson(r);

        Map<String, String> headers = new HashMap<String, String>();
        Map<String, String> querys = null;
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpPostJson(host, path, connectTimeout, headers, querys, reqJson,
                signHeaderPrefixList, appKey, appSecret, hospId);
```

#### 1.6.3 响应数据

参数格式：application/json

参数说明：

| 字段名称           | 类型   | 描述                                   | 备注 |
| :----------------- | :----- | :------------------------------------- | :--- |
| tel1(patients)     | string | 电话号码                               |      |
| birthday(patients) | string | 出生日期，格式为 "YYYY-MM-DD HH:MM:SS" |      |
| sex(patients)      | int    | 性别标识，1表示男性，通常0表示女性     |      |
| sexName(patients)  | string | 性别名称，"男"或"女"                   |      |
| name(patients)     | string | 姓名                                   |      |
| id(patients)       | long   | 个人ID                                 |      |
| idNumber(patients) | string | 身份证号码                             |      |

响应数据样例：

```json
{
    "data": [
        {
            "tel1": "38125375100",
            "birthday": "1986-03-27 00:00:00",
            "sex": "1",
            "sexName": "男",
            "name": "杨奇韵",
            "id": 1796786711460069400,
            "idNumber": "513101198603270319"
        }
    ],
    "resultCode": "1",
    "resultMsg": "成功！"
}
```



## 1.7 视力眼压接口

#### 1.7.1 基本信息

> 请求host: http://sit.aierchina.com:8710/external-api
>
> 请求path：/external-api/avis/interface/deviceDocking/getAutoVisionByVisitNumber
>
> 请求方式：get
>
> 接口描述：该接口用于获取患者的体查信息（视力和眼压）

#### 1.7.2 请求参数

格式：路径参数querys

参数说明：

| 参数名称    | 值     | 描述                              |
| ----------- | ------ | --------------------------------- |
| checkBdate  | String | 检查的开始日期，格式为 YYYY-MM-DD |
| checkEdate  | String | 检查的结束日期，格式为 YYYY-MM-DD |
| visitNumber | String | 访问编号或病例编号                |

请求参数代码：

```java
String host = "http://sit.aierchina.com:8710";
        String path = "/external-api/avis/interface/deviceDocking/getAutoVisionByVisitNumber";
        int connectTimeout=7200;
        String HospId = "9999";
        String appKey = "aviseq_9999";
        String appSecret = "lsj.z4HzPyAA";

        Map<String,String> querys = new HashMap<>();
        querys.put("checkBdate","2024-06-09");
        querys.put("checkEdate","2024-07-08");
        querys.put("visitNumber","MZ202407070797");
        String reqJson = JsonUtil.toJson(querys);
        Map<String,String>headers =new HashMap<>();
        List<String> signHeaderPrefixList = new ArrayList<>();
        Response resp = HttpClientUtils.httpGet(host, path, connectTimeout, headers, querys, signHeaderPrefixList,appKey,appSecret,HospId);
        System.out.println(resp.getBody());
```

#### 1.7.3 响应数据

参数格式：application/json

参数说明：

| 参数名称    | 类型   | 描述           |
| ----------- | ------ | -------------- |
| patientName | String | 姓名           |
| visitNumber | String | 就诊号         |
| patientId   | String | 患者ID         |
| scdOs       | String | 左眼裸眼视力   |
| scdOd       | String | 右眼裸眼视力   |
| scdOsValue  | String | 左眼裸眼视力值 |
| scdOdValue  | String | 右眼裸眼视力值 |
| ccdOs       | String | 左眼矫正视力   |
| ccdOd       | String | 右眼矫正视力   |
| ccdOsValue  | String | 左眼矫正视力值 |
| ccdOdValue  | String | 右眼矫正视力值 |
| iopOs       | String | 左眼眼压       |
| iopOd       | String | 右眼眼压       |

响应数据样例：	

```json
{
    "data": [
        {
            "patientName": "祝倩",
            "iopOd": null,
            "scdOsValue": "0.1",
            "patientId": 1809846719874044000,
            "visitNumber": "MZ202407070797",
            "ccdOs": "10",
            "scdOd": "02",
            "scdOdValue": "0.12",
            "ccdOsValue": "0.8",
            "ccdOdValue": "1.0",
            "iopOs": null,
            "ccdOd": "11",
            "scdOs": "01"
        }
    ],
    "resultCode": "1",
    "resultMsg": "成功！"
}
```

