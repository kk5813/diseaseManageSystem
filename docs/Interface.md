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
    "url": "/logout",
    "method": GET
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

```
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
    }
}
```

系统根据用户角色分配权限，确保用户仅能访问职责范围内的功能模块和数据。

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
    "data": {
        User
    },
    "code": 200,
    "msg": ""
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
    "data": {
        "total": 3  //总数
        "users":[
            user1,
            user2
            ...
        ]
    },
    "code": 200,
    "msg": ""
}
```

##### 2.2.4 用户分页查询

**请求：** 

 后端让 pageSize默认为10， pageNumber默认为1， 如果前端没有传入这二个参数，进行权限判断，权限不够报错，权限够，返回全部信息。

```json
{
    url : "/page",
    method: get,
    params:{
        pageSize,     // 默认10
        pageNumber,   // 默认1
    }
}
```

**响应：** 

暂定所有返回多个数据的，都在前面加上一共list

```json
{
    "data": {
        "total": 3  //总数
        "users":[
            user1,
            user2
            ...
        ]
    },
    "code": 200,
    "msg": ""
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
    "data": {
        Patients
    },
    "code": 200,
    "msg": ""
}
```

##### 3.2.2 患者信息模糊查询

**请求：**

```json
{
    url : "/search",
    method: get,
    body:{
        "sex": 0,
        "name": "朱畅畅"
        "birthdayBegin": "2024-12-17"
        "birthdayEnd": "2024-12-17"
        "visitTimeBegin": "2024-12-17"    // begin end成对出现
        "visitTimeEnd": "2024-12-17"
    }
}
```

**响应:**

```json
{
    "data": {
        "total": 3  //总数
        "users":[
            patient1,
            patient2
            ...
        ]
    },
    "code": 200,
    "msg": ""
}
```

##### 3.2.3 分页查询患者信息

 后端让 pageSize默认为10， pageNumber默认为1， 如果前端没有传入这二个参数，进行权限判断，权限不够报错，权限够，返回全部信息。

**请求**

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

**响应：** 

暂定所有返回多个数据的，都在前面加上一共list

```json
{
    "data": {
        "total": 3  //总数
        "users":[
            patient1,
            patient2
            ...
        ]
    },
    "code": 200,
    "msg": ""
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
    "data": {
        "total": 3  // 全体成员
        "users":[
            patient1,
            patient2
            ...
        ]
    },
    "code": 200,
    "msg": ""
}
```



##### 3.2.5 患者信息更新（补充）

**请求：**

```json
{
    url : "/edit",
    method: put,
    body:{
       Patients
    }
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

##### 3.2.6 患者信息批量导出

**请求：**

```json
{
    "url": "/export",
    "method": "post",
    "body": {
        "sex": "男",
        "name": "朱畅畅"
        "birthdayBegin": "2024-12-17"
        "birthdayEnd": "2024-12-17"
        "visitTimeBegin": "2024-12-17"    // begin end成对出现
        "visitTimeEnd": "2024-12-17"
        // 若无任何条件即所有
    }
}
```

**响应：** 

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
        "patientId": "1809970417345020000",
        "patientName": "林柔汐",
   		//数据库中其他字段（參考hospitalInterface.md）
    }
}
```

**响应：**

```json
{
    "code": 200,
    "msg": "病历创建成功",
    "data": {
        null
    }
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
        "id": "1809971108367556610",
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
        null
    }
}
```

#### 4.3 病历模糊查询

· 医护人员可按患者姓名、编号、诊断类型等条件快速检索病历信息。

```json
{
    "url": "/search",
    "method": "get",
    "body": {
        "patientId": "1809970417345020000",
        "dateStart": "2024-07-01",           （必传字段）
        "dateEnd": "2024-07-31",             （必传字段）
        "patientName": "林柔汐",
    }
}
```

**响应：**

```json
{
    "code": 200,
    "msg": "病历维护成功",
    "data": {
        "total": 3, //总共返回多少数据
        "elements": [
            {
                "id": "1809971108367556610",
                "patientId": "1809970417345020000",
                "patientName": "林柔汐",
                //病人其他信息
            }
            // 更多病历记录...
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
    "data": {
        Element
    },
    "code": 200,
    "msg": ""
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
    "data": {
        "total": 3  //总数
        "users":[
            element1,
            element2
            ...
        ]
    },
    "code": 200,
    "msg": ""
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
    "url": "/Undo",
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

#### 6.2 失效

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

#### 6.3 更新

```json
{
    "url": "/edit",
    "method": "put",
    "body": {
        "id": "1809971108367556610",
        "doctorName": "朱畅畅"              // 字段全有,为null也更新
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

#### 6.5精确查找

```json
{
    url : "/find/{doctorId}",
    method: get,
}
```

```json
{
    "data": {
        Doctor
    },
    "code": 200,
    "msg": ""
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
    "data": {
        "total": 3
        "list":[
            visit1,
        	visit2,
        	visit3
        	...
        ]
    },
    "code": 200,
    "msg": ""
}
```

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
    "data": {
        "total": 3
        "list":[
            checkResult1,
        	checkResult2,
        	checkResult3
        	...
        ]
    },
    "code": 200,
    "msg": ""
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
    "data": {
        "total": 2
    "data": [
        {
            "patientId": "1855597141015232514",
            "itemCode": "310300064A",
            "itemName": "光学相干断层成像（OCT）",
            "visitNumber": "MZ202411120358",
            "checkTime": "2024-11-12 17:21:29",
            "files": [    // 一次检查可能有多张图片
                {
                    "type": "application/pdf",
                    "filePath": "E:\Download\project\e1e16675-e7a8-40d3-b2c3-b2242f56a171.pdf"
                },
                 {
                    "type": "application/pdf",
                    "url": "E:\Download\project\e1e16675-e7a8-40d3-b2c3-b2242f56a171.pdf"
                }
            ],
        },
        {
            "item_code": "310300064A",
            "patient_id": "1855597141015232514",
            "item_name": "光学相干断层成像（OCT）",
            "visit_number": "MZ202411120358",
            "check_time": "2024-11-12 17:21:29",
            "files": [
                {
                    "type": "image/jpeg",
                    "file_path": "E:\Download\project\140a36e0-79ef-420b-adaf-41362093f6ab.jpg"
                }
            ]
        }
    ]
    },
    "code": 200,
    "msg": ""
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



