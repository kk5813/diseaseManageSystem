# 前后端接口文档

## 一、接口设计说明

### 1.请求域名说明：

- 请求host: `https://localhost:8081`   、`http://localhost:8081` 

- Api命名格式：

  `/api/v{n}/InterfaceName`  其中n写版本号，InterfaceName为接口名或者具体路径

### 2.请求方式说明：

常用的HTTP请求方式（括号里是对应的SQL命令）。

```
GET（SELECT）：从服务器取出资源（一项或多项）。
POST（CREATE）：在服务器新建一个资源。
PUT（UPDATE）：在服务器更新资源（客户端提供改变后的完整资源）。
DELETE（DELETE）：从服务器删除资源。
```

### 3. 返回格式说明：

下述格式为统一返回格式：

```json
{
    "data": {},
    "code": 0,
    "msg": ""
}
```

其中data中存放返回数据

code 为状态码

msg为提示信息

#### 3.1状态码说明：

```
200 OK - [GET]：服务器成功返回用户请求的数据。
201 CREATED - [POST/PUT/PATCH]：用户新建或修改数据成功。
202 Accepted - [*]：表示一个请求已经进入后台排队（异步任务）
204 NO CONTENT - [DELETE]：用户删除数据成功。

400 INVALID REQUEST - [POST/PUT/PATCH]：用户发出的请求有错误，服务器没有进行新建或修改数据的操作。
401 Unauthorized - [*]：表示用户没有权限（令牌、用户名、密码错误）。
403 Forbidden - [*] 表示用户得到授权（与401错误相对），但是访问是被禁止的。
404 NOT FOUND - [*]：用户发出的请求针对的是不存在的记录，服务器没有进行操作。
406 Not Acceptable - [GET]：用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。
410 Gone -[GET]：用户请求的资源被永久删除，且不会再得到的。

500 INTERNAL SERVER ERROR - [*]：服务器发生错误，用户将无法判断发出的请求是否成功。
```

实际中后续简化

## 二、后端接口设计

```
接口名称（name），接口地址（url），输入参数（body），返回值(data)，错误码(errorCode)，错误信息(errorMessage)。
```

### 0. 统一说明：

- 下面所有的接口 前缀暂时定为：`https://localhost:8081/api/v{n}`  这里的v{n}取值为v1，方便后续调整更新
- body 里面写的请求参数，前端去掉body,直接写里面的参数，这里是为了方便描述做了封装区分。 
- 返回的code根据实际情况来决定，之后封装一个异常管理和code的枚举类
- msg根据实际业务返回和异常情况进行处理。（为了方便调试的话，甚至可以是json格式的错误说明）

### 1.账户管理

权限校验

```http
http://localhost:8081/api/v{n}/account
```

#### 1.1 用户登录与权限验证

**请求：** 

```json
{
    url: "/login",
    method: POST,
    body:{
       userLoginName: "zcc",
       userPassword: "2287996531",   // 明文密码，经过前端MD5加密一次，后端再salt加密一次
    }
}
```

**响应：** 

```json
{
    "data": {
       MapUtil.builder()
                .put("userId", user.getUserId())
                .put("userLoginName", user.getUserLoginName())
                .put("userName", user.getUserName())
                .map()
    },
    "code": 200,
    "msg": ""
}
```

#### 1.2 用户登出

**请求：** 

```json
{
    url: "/logout",
    method: GET
}
```

**响应：** 

```json
{
    "data": {
    },
    "code": 200,
    "msg": ""
}
```

### 2. 用户管理

统一接口前缀

```http
http://localhost:8081/api/v{n}/user
```

#### 2.1 用户注册

医院工作人员通过系统前端完成注册，填写姓名、角色（如医生、护士、管理员等）、联系方式、科室等信息。

**请求：** 

```json
{
    url : "/add",
    method: POST,
    body:{
        userLoginName: "zcc"
        userName: "朱畅畅"
        userPassword: "2287996531"
        user_status: 1
    }
}
```

系统根据用户角色分配权限，确保用户仅能访问职责范围内的功能模块和数据。

**响应：** 

```json
{
    "data": {
       返回用户的全部信息（除开数据库status字段）
    },
    "code": 200,
    "msg": ""
}
```

#### 2.2 用户管理与维护

· 管理员可以对系统用户进行增删改查操作，包括修改角色及权限。

· 支持用户状态管理（如启用、禁用、锁定账户）和操作记录审计。

##### 2.2.1 用户信息更新

**请求：** 

```json
{
    url : "/edit",
    method: post,
    body:{
        userLoginName: "zcc"
        userName: "朱畅畅"
        userPassword: "2287996531"
        user_status: 1
    }
}
```

系统根据用户角色分配权限，确保用户仅能访问职责范围内的功能模块和数据。

**响应：** 

```json
{
    "data": {
    },
    "code": 200,
    "msg": ""
}
```

##### 2.2.2 用户信息删除

**请求：** 

```json
{
    url : "invalid/{userId}",
    method: GET
}
```

**响应：** 

data 为{} ，由code决定成功与失败 和其他信息

```json
{
    "data": {     
    },
    "code": 200,
    "msg": ""
}
```



##### 2.2.3 用户查找（One）

**请求：** 

```json
{
    url : "/find/{userId}",
    method: get,
}
```

**响应：** 

```json
{
    "data": {
        User
    },
    "code": 200,
    "msg": ""
}
```

##### 1.3.5 用户批量查找

**请求：** 

 后端让 pageSize默认为10， pageNumber默认为1， 如果前端没有传入这二个参数，进行权限判断，权限不够报错，权限够，返回全部信息。

```json
{
    url : "/list",
    method: get,
    body:{
        pageSize,
        pageNumber,
    }
}
```

**响应：** 

暂定所有返回多个数据的，都在前面加上一共list

```json
{
    "data": {
        "total":, //总共返回多少数据
       	"list":[
          {数据库中用户信息},{数据库中用户信息}
        ]
    },
    "code": 200,
    "msg": ""
}
```

### 2. 患者信息管理

#### 2.1 患者数据接入

· 系统通过数据接口从第三方获取患者信息，包括姓名、性别、年龄、联系方式、病史、过敏史及既往就诊记录等。

· 每日凌晨12点系统自动同步新增或更新数据，确保数据实时性和完整性。

· 系统设置严格的接口安全控制，防止数据泄露或篡改。

**这个接口前端应该不需要，后端单独的接口**

##### 

#### 2.2 患者档案管理

· 系统使用第三方数据中的患者 ID 作为唯一患者编号，建立患者档案，用于集中存储患者的基本信息、就诊记录及随访计划。

· 医护人员可补充院内检查记录和治疗方案，形成完整档案。

· 支持患者数据的批量导入导出功能，便于与其他系统共享或整合数据。

##### 2.2.1 查询患者信息（One）

**请求：** 

```json
{
    url : "/patient/find/{patientId}",
    method: get,
    body:{
    }
}
```

**响应：** 

```json
{
    "data": {
        数据库中病人信息
    },
    "code": 200,
    "msg": ""
}
```

##### 2.2.2 分页查询患者信息

 后端让 pageSize默认为10， pageNumber默认为1， 如果前端没有传入这二个参数，进行权限判断，权限不够报错，权限够，返回全部信息。

**请求**

```json
{
    url : "/patient/list",
    method: get,
    body:{
        pageSize,
        pageNumber,
    }
}
```

**响应：** 

暂定所有返回多个数据的，都在前面加上一共list

```json
{
    "data": {
        "total":, //总共返回多少数据
       	"list":[
          {数据库中病人信息},{数据库中病人信息}
        ]
    },
    "code": 200,
    "msg": ""
}
```

##### 2.2.3 患者信息更新（补充）

**请求：**

```json
{
    url : "/patient/edit",
    method: put,
    body:{
       数据库中所有字段，命名规则小驼峰 (为空的话，后端就不更新这个值就可以了 mybatis中有<set> 和<if>)
    }
}
```

**响应：** 

```json
{
    "data": {
         数据库中所有字段，命名规则小驼峰
    },
    "code": 200,
    "msg": ""
}
```

##### 2.2.4 患者信息批量导入

**请求：**

```json
{
    "url": "/patient/import",
    "method": "post",
    "body": [
        {
            "patientId": "1",
            "patientName": "张三",
            "birthday": "1990-01-01",
            "sex": "男",
            // 其他字段...
        },
        // 更多患者对象...
    ]
}
```

**响应：** 

```json
{
    "code": 200,
    "msg": "批量导入成功",
    "data": {
        "total": 2, //有多少数据导入成功
        "errors": [// 哪些数据导入失败
            {
                "patientId": "3",
                "errorMessage": "无效的日期格式"
            }
            // 更多错误信息...
        ]
    }
}
```

##### 2.2.5 患者信息批量导出

**请求：**

```json
{
    "url": "/patient/export",
    "method": "get",
    "body": {
        "sex": "男",
        "ageRange": {
            "min": 20,
            "max": 30
        }
        // 其他筛选条件...
    }
}
```

**响应：** 

两种任选

```json
//后端响应包含一个下载链接或直接返回文件流，允许前端下载导出的患者信息文件。
{
    "code": 200,
    "msg": "批量导出成功",
    "data": {
        "downloadUrl": "https://example.com/downloads/patients.csv"
    }
}
```

```json
//后端直接返回文件流：
{
    "code": 200,
    "msg": "批量导出成功",
    "headers": {
        "Content-Type": "text/csv",
        "Content-Disposition": "attachment; filename=patients.csv"
	}
}

```

### 3. 病历管理

#### 3.1 病历创建

· 医护人员在患者首次就诊时创建电子病历，记录患者基本信息、主诉、体征、诊断结果及初步治疗方案。

· 支持关联患者既往病史和检查记录，形成完整的病例信息链条。

**请求：**

```json
{
    "url": "/medicalRecord/add",
    "method": "post",
    "body": {
        "patientId": "1809970417345020000",
        "patientName": "林柔汐",
   		//数据库中其他字段
    }
}
```

**响应：**

```json
{
    "code": 200,
    "msg": "病历创建成功",
    "data": {
        //返回创建的该病例
    }
}
```

#### 3.2 病历更新与维护

· 医生根据患者后续就诊情况更新病历，包括治疗效果、复诊情况和新检查结果等。

· 支持多次就诊记录按时间线整合，便于查看病程发展及关键事件。

##### 3.2.1 病例更新

**请求：**

```json
{
    "url": "/medicalRecord/edit",
    "method": "put",
    "body": {
        "medicalRecordId": "1809971108367556610",
       	//数据库中其他字段
    }
}
```

**响应：**

```json
{
    "code": 200,
    "msg": "病历更新成功",
    "data": {
        //返回更新的该病例字段信息
    }
}
```

##### 3.2.2 病例维护

**请求：**

```json
{
    "url": "/medicalRecord/maintenance",
    "method": "get",
    "body": {
        "patientId": "1809970417345020000",
        "dateStart": "2024-07-01",
        "dateEnd": "2024-07-31",
    }
}
```

**响应：**

```json
{
    "code": 200,
    "msg": "病历维护成功",
    "data": {
        "total":, //总共返回多少数据
        "list": [
            {
                "medicalRecordId": "1809971108367556610",
                "patientId": "1809970417345020000",
                "patientName": "林柔汐",
                //病人其他信息
            }
            // 更多病历记录...
        ]
    }
}
```

#### 3.3 病历检索与共享

· 医护人员可按患者姓名、编号、诊断类型等条件快速检索病历信息。

<u>系统支持病历共享与打印，用于跨科室协作或患者需求。？？？</u>

##### 3.3.1 病例条件检索

**请求：**

(为空的话，后端就不判断这个值就可以了 mybatis中有<where> 和<if>)

```json
{
    "url": "/medicalRecord/search",
    "method": "get",
    "body": {
        "patientId": "1809970417345020000",
        "dateStart": "2024-07-01",
        "dateEnd": "2024-07-31",
        "patientName": "林柔汐",
        "diagnosisType": "眼科"
    }
}
```

**响应：**

```json
{
    "code": 200,
    "msg": "病历维护成功",
    "data": {
        "total":, //总共返回多少数据
        "list": [
            {
                "medicalRecordId": "1809971108367556610",
                "patientId": "1809970417345020000",
                "patientName": "林柔汐",
                //病人其他信息
            }
            // 更多病历记录...
        ]
    }
}
```

### 4. 随访计划管理

#### 4.1 随访患者列表

· 护士通过随访管理页面查看待随访患者名单：

**o** ***\*今日待随访\****：约定随访时间为当天的患者列表。

**o** ***\*超期未随访\****：约定随访时间在当天之前但未完成随访的患者名单。

**o** ***\*全部未随访\****：包含约定随访日期在未来的所有未随访患者。



#### 4.2 随访执行

· 护士通过电话随访患者，填写随访内容：

**o** ***\*随访成功\****：记录随访类型、内容及健康状态。

**o** ***\*随访未成功\****：填写原因并安排下次随访时间。



#### 4.3 计划制定与提醒

· 医生根据患者病情和档案制定个性化随访计划。

· 系统根据计划通过短信或 客户端 通知患者和医护人员随访日程。
