# 用户端微信小程序登录注册设计

## 1. 概述

实现用户端微信小程序登录注册功能，采用双 Token 机制，JWT + Redis 双重验证。

## 2. 整体架构

```
小程序前端                    后端                        Redis
    │                        │                           │
    │──loginCode+phoneCode──>│                           │
    │                        │──code2Session──>微信       │
    │                        │<──openid+session_key──   │
    │                        │                           │
    │                        │──解密手机号               │
    │                        │                           │
    │                        │──生成Access Token(JWT 30min)──>│
    │                        │──生成Refresh Token(UUID)──────>│(7天)
    │<────────user+双token───│                           │
```

## 3. 数据模型

### 3.1 User 实体

已有字段：id, openid, name, phone, sex, idNumber, avatar, createTime

### 3.2 新增 DTO/VO

**UserLoginDTO**
```java
{
  "loginCode": "微信临时登录凭证",
  "phoneCode": "微信手机号加密数据"
}
```

**UserLoginVO**
```java
{
  "id": Long,
  "openid": String,
  "name": String,
  "phone": String,
  "avatar": String,
  "token": String,         // Access Token (JWT 30min)
  "refreshToken": String   // Refresh Token (UUID, Redis 7天)
}
```

## 4. 接口设计

### 4.1 登录注册接口

**请求**
```
POST /user/user/login
Content-Type: application/json

{
  "loginCode": "微信临时登录凭证",
  "phoneCode": "微信手机号加密数据"
}
```

**响应（成功）**
```json
{
  "code": 1,
  "data": {
    "id": 1,
    "openid": "oXXXX",
    "name": "sb随机生成",
    "phone": "138****8888",
    "avatar": "https://default-avatar.png",
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "uuid-xxx-xxx"
  }
}
```

**响应（失败）**
```json
{
  "code": 0,
  "msg": "登录失败，请重试"
}
```

### 4.2 刷新Token接口

**请求**
```
POST /user/user/refresh
Header: X-Refresh-Token: uuid-xxx-xxx
```

**响应（成功）**
```json
{
  "code": 1,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "uuid-yyy-yyy",
    "user": {
      "id": 1,
      "openid": "oXXXX",
      "name": "sb123456",
      "phone": "138****8888",
      "avatar": "https://default-avatar.png"
    }
  }
}
```

**响应（失败 - Refresh Token 已过期）**
```json
{
  "code": 0,
  "msg": "登录已过期，请重新登录"
}
```

### 4.3 登出接口

**请求**
```
POST /user/user/logout
Header: token: <Access Token>
```

**响应**
```json
{
  "code": 1,
  "msg": "登出成功"
}
```

## 5. 登录注册逻辑

```
1. 接收 loginCode 和 phoneCode
2. 调用微信 code2Session 接口，换取 openid 和 session_key
3. 用 session_key 解密 phoneCode，获取手机号
4. 根据 openid 查询用户：
   - 存在：登录，返回用户信息
   - 不存在：
     a. 随机生成 name：sb + 6位随机字符
     b. 设置默认头像：https://default-avatar.png
     c. 创建新用户，注册登录
5. 生成 Access Token (JWT，30分钟过期)
6. 生成 Refresh Token (UUID)
7. 将 Refresh Token 存入 Redis，key=user:refresh:{openid}，过期时间7天
8. 返回用户信息 + Access Token + Refresh Token
```

## 6. 拦截器验证逻辑

```
1. 提取请求头中的 token (Access Token)
2. 验证 JWT 签名和过期时间
3. 从 JWT 中获取 userId
4. 查询 Redis：user:refresh:{openid}
5. 比较 Redis 中的 token 与请求的 Access Token 是否一致
6. 一致：验证通过，设置 BaseContext
   不一致或不存在：返回 401
```

## 7. Token 策略

| Token | 类型 | 过期时间 | 存储位置 |
|-------|------|----------|----------|
| Access Token | JWT | 30分钟 | 无需存储，客户端保存 |
| Refresh Token | UUID | 7天 | Redis (key: user:refresh:{openid}) |

## 8. JWT 配置

- **签名算法**：HS256
- **Claims 包含**：userId, openid
- **Token 名称**：token (Header: token)
- **Refresh Token Header**：X-Refresh-Token

## 9. 微信解密实现

需要集成微信数据包解密工具类：
1. code2Session 获取 openid 和 session_key
2. 使用 session_key 解密 phoneCode 获取手机号

## 10. 文件清单

| 类型 | 文件 | 说明 |
|------|------|------|
| Controller | UserController | 用户登录注册登出控制器 |
| Service | UserService, UserServiceImpl | 用户业务逻辑 |
| Mapper | UserMapper | 用户数据库操作 |
| DTO | UserLoginDTO | 登录请求参数 |
| VO | UserLoginVO | 登录响应 |
| Interceptor | JwtTokenUserInterceptor | 用户端JWT+Redis拦截器 |
| Config | WebMvcConfiguration | 配置用户端拦截器路径 |
| Utils | WeChatUtil | 微信code2Session和手机号解密工具 |
| Properties | JwtProperties | JWT配置（需增加用户端配置） |

## 11. 拦截器路径配置

```java
registry.addInterceptor(jwtTokenUserInterceptor)
    .addPathPatterns("/user/**")
    .excludePathPatterns("/user/user/login", "/user/user/refresh");
```