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

```
http://localhost:8081/api/v{n}/account
```

#### 1.1 用户登录

**请求：** 

```json
{
    "url": "/login",
    "method": "POST",
    "body": {
        "userLoginName": "zcc",
        "userPassword": "2287996531"  // 前端已用 MD5 加密的密码
    }
}
```

**响应：** 

```json
{
    "code": 0,
    "msg": "string",
    "data": {
        "userLoginName": "string",
        "userName": "string",
        "userId": 0
    }
}
```

#### 1.2 用户登出

**请求：** 

```json
{
    "url": "/logout",
    "method": GET
}
```

**响应：** 

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```

### 2. 用户管理

统一接口前缀

```
http://localhost:8081/api/v{n}/user
```

#### 2.1 用户注册

医院工作人员通过系统前端完成注册，填写姓名、角色（如医生、护士、管理员等）、联系方式、科室等信息。
**该方法的userLoginName不能重复，否则会报错误400，该用户已注册 **

**请求：** 

```json
{
    url : "/add",
    method: POST,
    body:{
        userLoginName: "zcc"
        userName: "朱畅畅"
        userPassword: "2287996531"
    }
}
```

系统根据用户角色分配权限，确保用户仅能访问职责范围内的功能模块和数据。

**响应：** 

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```

#### 2.2 用户管理与维护

· 管理员可以对系统用户进行增删改查操作，包括修改角色及权限。

· 支持用户状态管理

##### 2.2.1 用户信息更新

- 管理员拥有此权限

**请求：** 

```json
{
    url : "/edit",
    method: post,
    body:{
        userLoginName: "zcc"
        userName: "朱畅畅"
        userPassword: "2287996531"
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

##### 2.2.2 用户信息删除(失效)

- 管理员拥有此权限

**请求：** 

```json
{
    url : "invalid/{userId}",
    method: GET
}
```

**响应：** 

```json
{
    "data": {
        null
    },
    "code": 200,
    "msg": ""
}
```

##### 2.2.3 用户精确查询

**请求：** 

```json
{
    url : "/find/{userId}",
    method: get
}
```

**响应：** 

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "userId": 14,
        "userLoginName": "zcc",
        "userName": "朱畅畅",
        "userPassword": "7ce7070696f2200bd57edb836265f9db",
        "salt": "bEZ5mLq6SxkSkZHBnZxRfd8dyXGq7vfi",
        "userStatus": 0,
        "creator": "系统管理员",
        "createTime": "2024-09-23 20:00:12",
        "modifier": "朱医生",
        "updateTime": "2024-12-18 14:42:10"
    }
}
```

##### 2.2.3 用户模糊查询

**请求：** 

```json
{
    url : "/search",
    method: get,
    body:{
        userLoginName: "zcc",    //参数都不给即返回所有
        userName: "朱畅畅" 
    }
}
```

**响应：** 

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "total": 2,
        "users": [
            {
                "userId": 14,
                "userLoginName": "zcc",
                "userName": "朱畅畅",
                "userPassword": "7ce7070696f2200bd57edb836265f9db",
                "salt": "bEZ5mLq6SxkSkZHBnZxRfd8dyXGq7vfi",
                "userStatus": 0,
                "creator": "系统管理员",
                "createTime": "2024-09-23 20:00:12",
                "modifier": "朱医生",
                "updateTime": "2024-12-18 14:42:10"
            },
            {
                "userId": 15,
                "userLoginName": "kk",
                "userName": "朱畅畅",
                "userPassword": "f17ca78fbcfcf679c0665f3b085e2738",
                "salt": "ZTgV75IYHSES2t6LwpgYbPBaKiWbt51U",
                "userStatus": -1,
                "creator": "朱医生",
                "createTime": "2024-09-23 20:05:55",
                "modifier": "朱畅畅",
                "updateTime": "2024-12-18 14:50:14"
            }
        ]
    }
}
```

##### 2.2.4 用户分页查询

**请求：** 

 后端让 pageSize默认为10， pageNumber默认为1， 如果前端没有传入这二个参数，进行权限判断，权限不够报错，权限够，返回全部信息。

```json
{
    url : "/page",
    method: get,
    Query:{
        pageSize,     // 默认10
        pageNumber,   // 默认1
    }
}
```

**响应：** 

暂定所有返回多个数据的，都在前面加上一共list

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "records": [
            {
                "userId": 1,
                "userLoginName": "admin",
                "userName": "系统管理员",
                "userPassword": "fc29efa28323abb41c3fa95c49efb693",
                "salt": "1ck12b13k1jmjxrg1h0129h2lj",
                "userStatus": 0,
                "creator": "admin",
                "createTime": "2021-02-01 10:07:38",
                "modifier": null,
                "updateTime": null
            },
            {
                "userId": 14,
                "userLoginName": "zcc",
                "userName": "朱畅畅",
                "userPassword": "7ce7070696f2200bd57edb836265f9db",
                "salt": "bEZ5mLq6SxkSkZHBnZxRfd8dyXGq7vfi",
                "userStatus": 0,
                "creator": "系统管理员",
                "createTime": "2024-09-23 20:00:12",
                "modifier": "朱医生",
                "updateTime": "2024-12-18 14:42:10"
            },
            {
                "userId": 15,
                "userLoginName": "kk",
                "userName": "朱畅畅",
                "userPassword": "f17ca78fbcfcf679c0665f3b085e2738",
                "salt": "ZTgV75IYHSES2t6LwpgYbPBaKiWbt51U",
                "userStatus": -1,
                "creator": "朱医生",
                "createTime": "2024-09-23 20:05:55",
                "modifier": "朱畅畅",
                "updateTime": "2024-12-18 14:50:14"
            },
            {
                "userId": 16,
                "userLoginName": "zc",
                "userName": "曾灿",
                "userPassword": "6f06908988cacdb4dd10d81e4db07a8e",
                "salt": "IXmpJOzduehRVdBmcFxmlYo2dAmnraU7",
                "userStatus": 1,
                "creator": "朱医生",
                "createTime": "2024-12-18 14:31:24",
                "modifier": null,
                "updateTime": null
            }
        ],
        "total": 4,
        "size": 10,
        "current": 1,
        "orders": [],
        "searchCount": true,
        "pages": 1
    }
}
```

### 3. 患者信息管理

```json
http://localhost:8081/api/v{n}/patients
```

#### 3.1 患者数据接入

· 系统通过数据接口从第三方获取患者信息，包括姓名、性别、年龄、联系方式、病史、过敏史及既往就诊记录等。

· 每日凌晨12点系统自动同步新增或更新数据，确保数据实时性和完整性。

· 系统设置严格的接口安全控制，防止数据泄露或篡改。

#### 3.2 患者档案管理

· 系统使用第三方数据中的患者 ID 作为唯一患者编号，建立患者档案，用于集中存储患者的基本信息、就诊记录及随访计划。

· 医护人员可补充院内检查记录和治疗方案，形成完整档案。

· 支持患者数据的批量导入导出功能，便于与其他系统共享或整合数据。

##### 3.2.1 患者信息精确查询

**请求：** 

```json
{
    url : "/find/{patientId}",
    method: get,
}
```

**响应：** 

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "id": 1,
        "name": "kk",
        "sex": 1,
        "sexName": "男",
        "birthday": "2024-12-18 14:45:00",
        "idNumber": "123",
        "phone": "4321",
        "status": 1,
        "createTime": "2024-12-05T07:03:24.000+00:00",
        "updateTime": "2024-12-18T07:03:27.000+00:00"
    }
}
```

##### 3.2.2 患者信息模糊查询

**请求：**

```json
{
    url : "/search",
    method: get,
    body:{
    "sex": 1,
    "name": "k",
    "birthdayBegin": "2024-12-17",
    "birthdayEnd": "2024-12-19",
    "visitTimeBegin": "",
    "visitTimeEnd": ""
}
}
```

**响应:**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "total": 1,
        "patients": [
            {
                "id": 1,
                "name": "kk",
                "sex": 1,
                "sexName": "男",
                "birthday": "2024-12-18 14:45:00",
                "idNumber": "123",
                "phone": "4321",
                "status": 1,
                "createTime": "2024-12-05T07:03:24.000+00:00",
                "updateTime": "2024-12-18T07:03:27.000+00:00"
            }
        ]
    }
}
```

##### 3.2.3 分页查询患者信息

 后端让 pageSize默认为10， pageNumber默认为1， 如果前端没有传入这二个参数，进行权限判断，权限不够报错，权限够，返回全部信息。

**请求**

```json
{
    url : "/page",
    method: get,
    Query:{
        pageSize : 1,
        pageNumber: 10
    }
}
```

**响应：** 

暂定所有返回多个数据的，都在前面加上一共list

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "total": 1,
        "patients": [
            {
                "id": 1,
                "name": "kk",
                "sex": 1,
                "sexName": "男",
                "birthday": "2024-12-18 14:45:00",
                "idNumber": "123",
                "phone": "4321",
                "status": 1,
                "createTime": "2024-12-05T07:03:24.000+00:00",
                "updateTime": "2024-12-18T07:03:27.000+00:00"
            }
        ]
    }
}
```

##### 3.2.4 患者信息整体查询

```json
{
    url : "/list",
    method: get,
}
```

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "total": 1,
        "patients": [
            {
                "id": 1,
                "name": "朱畅畅",
                "sex": 0,
                "sexName": "男",
                "birthday": "2024-12-18 15:03:13",
                "idNumber": "123456789",
                "phone": "15955422050",
                "status": 1,
                "createTime": "2024-12-05T07:03:24.000+00:00",
                "updateTime": "2024-12-18T07:03:27.000+00:00"
            }
        ]
    }
}
```



##### 3.2.5 患者信息更新（补充）

**请求：**

```json
{
    url : "/edit",
    method: put,
    body:{
    "id": 1,     //Id为必传字段
    "name": "kk",
    "sex": 1,
    "sexName": "男",
    "birthday": "2024-12-18 14:45:00",
    "idNumber": "123",
    "phone": "4321",
    "status": 1
}
}
```

**响应：** 

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```

##### 3.2.6 患者信息批量导出

**请求：**

```json
{
    "url": "/export",
    "method": "post",
    "body":{
    "sex": 1,
    "name": "朱",
    "birthdayBegin": "2024-12-17",
    "birthdayEnd": "2024-12-19",
    "visitTimeBegin": "",
    "visitTimeEnd": ""
}
}
```

**响应：** 

```json
id,name,sexName,birthday,idNumber,phone
1,朱畅畅,男,2024-12-18T14:45,123,4321
```

#### 3.2.7 患者信息编辑

```json
{
    url : "/edit",
    method: put,
    "body":
{
    "id": 1,
    "name": "kk",
    "sex": 1,
    "sexName": "男",
    "birthday": "2024-12-18 14:45:00",
    "idNumber": "123",
    "phone": "4321",
    "status": 1
}
}
```

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```



### 4. 病历管理

```json
http://localhost:8081/api/v{n}/element
```

#### 4.1 病历创建

· 医护人员在患者首次就诊时创建电子病历，记录患者基本信息、主诉、体征、诊断结果及初步治疗方案。

· 支持关联患者既往病史和检查记录，形成完整的病例信息链条。

**请求：**

```json
{
    "url": "/add",
    "method": "post",
    "body": {
    "id": "1",
    "patientId": 1,
    "patientName": "式小什物织。",
    "mainAppeal": "候除理说本些理七。",
    "pastHistory": "及改流目飞。",
    "presentIllness": "大则段马。",
    "allergy": "天日然。",
    "specialOs": "转步类以需感先在。",
    "specialOd": "总员题据识式认。",
    "visitNumber": "业成角花。",
    "physicalExam": "统总山。",
    "dispose": "低交如九年。"
}
}
```

**响应：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```

#### 4.2 病历更新

· 医生根据患者后续就诊情况更新病历，包括治疗效果、复诊情况和新检查结果等。

· 支持多次就诊记录按时间线整合，便于查看病程发展及关键事件。

**请求：**

```json
{
    "url": "/edit",
    "method": "put",
    "body": {
    "id": "1",
    "patientId": 1,
    "patientName": "朱畅畅",
    "mainAppeal": "候除理说本些理七。",
    "pastHistory": "及改流目飞。",
    "presentIllness": "大则段马。",
    "allergy": "天日然。",
    "specialOs": "转步类以需感先在。",
    "specialOd": "总员题据识式认。",
    "visitNumber": "业成角花。",
    "physicalExam": "统总山。",
    "dispose": "低交如九年。"
}
}
```

**响应：**

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": "病历更新成功"
}
```

#### 4.3 病历模糊查询

· 医护人员可按患者姓名、编号、诊断类型等条件快速检索病历信息。

```json
{
    "url": "/search",
    "method": "get",
    "body": {
    "patientId": "1",
    "dataStart": "2024-07-01",
    "dataEnd": "2025-01-01",
    "patientName": "朱畅畅"
}
}
```

**响应：**

```json
{
    "code": 200,
    "msg": "维护成功",
    "data": {
        "total": 1,
        "elements": [
            {
                "id": "1",
                "patientId": 1,
                "mainAppeal": "候除理说本些理七。",
                "pastHistory": "及改流目飞。",
                "presentIllness": "大则段马。",
                "allergy": "天日然。",
                "specialOs": "转步类以需感先在。",
                "specialOd": "总员题据识式认。",
                "visitNumber": "业成角花。",
                "physicalExam": "统总山。",
                "dispose": "低交如九年。",
                "createTime": "2024-12-18T07:36:31.000+00:00",
                "updateTime": "2024-12-18T07:38:09.000+00:00"
            }
        ]
    }
}
```

#### 4.4病历精确查询

```json
{
    url : "/find/{elementId}",
    method: get,
}
```

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "id": "1",
        "patientId": 1,
        "mainAppeal": "候除理说本些理七。",
        "pastHistory": "及改流目飞。",
        "presentIllness": "大则段马。",
        "allergy": "天日然。",
        "specialOs": "转步类以需感先在。",
        "specialOd": "总员题据识式认。",
        "visitNumber": "业成角花。",
        "physicalExam": "统总山。",
        "dispose": "低交如九年。",
        "createTime": "2024-12-18T07:36:31.000+00:00",
        "updateTime": "2024-12-18T07:38:09.000+00:00"
    }
}
```

#### 4.5 病历分页查询

```json
{
    url : "/page",
    method: get,
    body:{
        pageSize,
        pageNumber,
    }
}
```

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "total": 1,
        "elements": [
            {
                "id": "1",
                "patientId": 1,
                "mainAppeal": "候除理说本些理七。",
                "pastHistory": "及改流目飞。",
                "presentIllness": "大则段马。",
                "allergy": "天日然。",
                "specialOs": "转步类以需感先在。",
                "specialOd": "总员题据识式认。",
                "visitNumber": "业成角花。",
                "physicalExam": "统总山。",
                "dispose": "低交如九年。",
                "createTime": "2024-12-18T07:36:31.000+00:00",
                "updateTime": "2024-12-18T07:38:09.000+00:00"
            }
        ]
    }
}
```

### 5. 随访计划管理

```json
http://localhost:8081/api/v{n}/followup
```

#### 5.1 今日随访

```json
{
    "url": "/todayUndo",
    "method": "get"
}
```

```json
{
    "data": {
        "total": 3  //总数
        "list":[
            followup1,
            followup2
            ...
        ]
    },
    "code": 200,
    "msg": ""
}
```



#### 5.2 超时间未随访

```json
{
    "url": "/overdue",
    "method": "get"
}
```

```json
{
    "data": {
        "total": 3  //总数
        "list":[
            followup1,
            followup2
            ...
        ]
    },
    "code": 200,
    "msg": ""
}
```

#### 5.3 全部未随访

```json
{
    "url": "/undo",
    "method": "get"
}
```

```json
{
    "data": {
        "total": 3  //总数
        "list":[
            followup1,
            followup2
            ...
        ]
    },
    "code": 200,
    "msg": ""
}
```

#### 5.4 完成随访

- 如果随访失败，还会自动添加下次随访

```json
{
    "url": "/editFollowup",
    "method": "get",
    "body":{
        ListFollowup
    }
}
```

```json
{
    "code": 200,
    "msg": "",
    "data": {
        null
    }
}
```

### 6. 医生管理

```json
http://localhost:8081/api/v{n}/doctor
```

#### 6.1 新增医生信息

- 请求参数

```json
{
    url : "/add",
    method: POST,
    body:{
    "id": "1",
    "doctorName": "朱畅畅"
}
}
```

- 响应

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```

#### 6.2 失效

- 请求参数

```json
{
    url : "/invalid/{doctorId}",
    method: GET,
}
```

- 响应

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```

#### 6.3 更新

```json
{
    "url": "/edit",
    "method": "put",
    "body": {
    "id": "1",
    "doctorName": "朱畅"
}
}
```

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": null
}
```

#### 6.4 分页查询

```json
{
    url : "/page",
    method: get,
    params:{
        pageSize,
        pageNumber,
    }
}
```

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "records": [
            {
                "id": 1,
                "doctorName": "朱畅",
                "status": 0,
                "createTime": "2024-12-18T07:56:53.000+00:00",
                "updateTime": "2024-12-18T07:59:38.000+00:00"
            }
        ],
        "total": 1,
        "size": 10,
        "current": 1,
        "orders": [],
        "searchCount": true,
        "pages": 1
    }
}
```

#### 6.5精确查找

```json
{
    url : "/find/{doctorId}",
    method: get,
}
```

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "id": 1,
        "doctorName": "朱畅",
        "status": 0,
        "createTime": "2024-12-18T07:56:53.000+00:00",
        "updateTime": "2024-12-18T07:59:38.000+00:00"
    }
}
```



### 7. 科室管理

```json
http://localhost:8081/api/v{n}/dept
```

#### 7.1 新增医生信息

- 请求参数

```json
{
    url : "/add",
    method: POST,
    body:{
        "id": 1
        "doctor_name": "王医生"
    }
}
```

- 响应

```json
{
    "data": {
        null
    },
    "code": 200,
    "msg": ""
}
```

#### 7.2 失效

- 请求参数

```json
{
    url : "/invalid/{doctorId}",
    method: POST,
}
```

- 响应

```json
{
    "data": {
        null
    },
    "code": 200,
    "msg": ""
}
```

#### 7.3 更新

```json
{
    "url": "/edit",
    "method": "put",
    "body": {
        "id": "1809971108367556610",
       	//数据库中其他字段
    }
}
```

```json
{
    "code": 200,
    "msg": "病历更新成功",
    "data": {
        null
    }
}
```

#### 7.4 分页查询

```json
{
    url : "/page",
    method: get,
    body:{
        pageSize,
        pageNumber,
    }
}
```

```json
{
    "data": {
        "total": 3  //总数
        "users":[
            doctor1,
            doctor2
            ...
        ]
    },
    "code": 200,
    "msg": ""
}
```

### 8. 眼别管理

```json
http://localhost:8081/api/v{n}/site
```

#### 8.1 新增医生信息

- 请求参数

```json
{
    url : "/add",
    method: POST,
    body:{
        "id": 1
        "doctor_name": "王医生"
    }
}
```

- 响应

```json
{
    "data": {
        null
    },
    "code": 200,
    "msg": ""
}
```

#### 8.2 失效

- 请求参数

```json
{
    url : "/invalid/{doctorId}",
    method: POST,
}
```

- 响应

```json
{
    "data": {
        null
    },
    "code": 200,
    "msg": ""
}
```

#### 8.3 更新

```json
{
    "url": "/edit",
    "method": "put",
    "body": {
        "id": "1809971108367556610",
       	//数据库中其他字段
    }
}
```

```json
{
    "code": 200,
    "msg": "病历更新成功",
    "data": {
        null
    }
}
```

#### 8.4 分页查询

```json
{
    url : "/page",
    method: get,
    body:{
        pageSize,
        pageNumber,
    }
}
```

```json
{
    "data": {
        "total": 3  //总数
        "users":[
            doctor1,
            doctor2
            ...
        ]
    },
    "code": 200,
    "msg": ""
}
```

### 9. 就诊信息管理

```json
http://localhost:8081/api/v{n}/visits
```

就诊信息展示在用户患者表单页里通过patientId精确查找

```json
{
    url : "/find/{patientId}",
    method: get
}
```

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": [
        {
            "id": 1,
            "patientId": 1,
            "doctorId": 1,
            "deptId": 1,
            "siteId": 1,
            "visitNumber": "1",
            "diagTime": "2024-12-18 18:45:31",
            "diagOrder": 1,
            "diagName": "1",
            "diagCode": "1",
            "createTime": "2024-12-13T10:45:40.000+00:00",
            "updateTime": "2024-12-18T10:45:44.000+00:00"
        }
    ]
}
```

#### 9.2 分页查询

```json
{
    url : "/page",
    method: get,
    body:{
        pageSize,
        pageNumber,
    }
}
```

```json
{
    "data": {
        "total": 3  //总数
        "users":[
            doctor1,
            doctor2
            ...
        ]
    },
    "code": 200,
    "msg": ""
}
```

#### 9.3 模糊查询

```json
{
    "url": "/search",
    "method": "get",
    "body": {
    "dataStart": "2024-07-01",
    "dataEnd": "2025-01-01",
    "patientName": "朱畅畅",
    "diagName": "1",
    "diagCode": "1",
}
}
```

**响应：**

### 10 检查结果

```json
http://localhost:8081/api/v{n}/check_result
```

检查结果也展示在用户患者表单页里通过patientId精确查找

```json
{
    url : "/find/{patientId}",
    method: get
}
```

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": [
        {
            "id": 1,
            "patientId": 1,
            "isUrgent": 1,
            "labItemName": "1",
            "labItemCode": "1",
            "reportName": "1",
            "refRange": "1",
            "labResultSignName": "1",
            "labFinalValue": "1",
            "visitingNo": "1",
            "labResultUnitName": "1",
            "auditDate": "2024-12-18 18:48:40",
            "createTime": "2024-12-18T10:48:44.000+00:00",
            "updateTime": "2024-12-18T10:48:46.000+00:00"
        }
    ]
}
```

### 11 检查报告

```json
http://localhost:8081/api/v{n}/check_report
```

检查报告仍然是在用户患者表单页中

```json
{
    url : "/find/{patientId}",
    method: get
}
```

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": [
        {
            "patientId": "1",
            "itemCode": "1",
            "itemName": "1",
            "visitNumber": "1",
            "checkTime": "2024-12-18T18:49:32",
            "files": [
                {
                    "type": "application/pdf",
                    "filePath": "E:\\Download\\project\\140a36e0-79ef-420b-adaf-41362093f6ab.pdf"
                }
            ]
        }
    ]
}
```

### 12 门诊处方

```json
{
    url : "/find/{patientId}",
    method: get
}
```

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "id": 1,
      "doctorId": 1,
      "patientId": 1,
      "deptId": 1,
      "regNumber": "1",
      "recipeNumber": "1",
      "recipeType": 1,
      "billingTime": "2024-12-11 12:53:14",
      "createTime": "2024-12-19T12:53:16",
      "updateTime": "2024-12-19T12:53:19"
    }
  ]
}
```

### 12 当天患者查询

- 此接口只针对当天的来的一位患者进行第三方库的单独查询并保存到本地,剩余信息由凌晨数据库进行补充同步

```json
{
    url : "today/onlySearch/{patientId}",
    method: get
}
```

```json

```

### 13 深度学习模型

后端逻辑处理调用Flask框架, 不面向前端,在上一个接口中同步进行,预留一个接口以便需要按钮的方式进行诊断

```json
{
    url : "http://localhost:8081/api/v{n}/diagnosis",   // 参数还在考虑中
    method: POST,
    body:{
        "id": 1
        "doctor_name": "王医生"
    }
}
```

### 14 分门别类统计患病人数

```json
{
    url : "http://localhost:8081/api/v{n}/diagnosis",   // 参数还在考虑中
    method: GET,
    body:{
        // 时间参数为空则为查询所有，但一定要传
        "beginTime": 20241222
        "endTime": 20241222
    }
}
```

```json
{
    "code": 200,
    "msg": "操作成功",
    "data": [
        {
            "groupName": "干眼",
            "totalCount": 4,
            "categoryCounts": [
                {
                    "categoryName": "干眼综合征",
                    "count": 4
                }
            ]
        },
        {
            "groupName": "白内障",
            "totalCount": 4,
            "categoryCounts": [
                {
                    "categoryName": "白内障",
                    "count": 1
                },
                {
                    "categoryName": "先天性白内障",
                    "count": 3
                }
            ]
        },
        {
            "groupName": "青光眼",
            "totalCount": 3,
            "categoryCounts": [
                {
                    "categoryName": "闭角型青光眼",
                    "count": 3
                }
            ]
        }
    ]
}
```



