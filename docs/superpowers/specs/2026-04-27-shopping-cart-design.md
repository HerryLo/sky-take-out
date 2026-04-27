# 购物车管理设计文档

## 版本信息

- **日期**: 2026-04-27
- **版本**: v1.0
- **状态**: 已批准

## 1. 项目背景

用户端购物车管理功能缺失，需要按照苍穹外卖标准实现完整的购物车CRUD功能，并引入Redis缓存提升查询性能。

## 2. 技术方案

### 2.1 架构设计

```
用户端小程序 → ShoppingCartController → ShoppingCartService → Redis缓存 + MySQL
                                                    ↓
                                              Dish/Setmeal Mapper (查价格)
```

### 2.2 缓存策略

| 操作 | 缓存行为 |
|------|----------|
| 添加商品 | 先写DB，再更新缓存 |
| 查看列表 | 先查缓存，未命中查DB并回填 |
| 修改数量 | 先更新DB，再删除缓存（懒更新） |
| 删除商品 | 先删DB，再删除缓存 |
| 清空购物车 | 先清DB，再清缓存 |

**缓存Key**: `shopping_cart:user:{userId}`
**过期时间**: 7天

## 3. 数据模型

### 3.1 实体类 (ShoppingCart)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| name | String | 商品名称 |
| image | String | 图片 |
| userId | Long | 用户ID |
| dishId | Long | 菜品ID |
| setmealId | Long | 套餐ID |
| dishFlavor | String | 口味 |
| number | Integer | 数量 |
| amount | BigDecimal | 金额 |
| createTime | LocalDateTime | 创建时间 |

### 3.2 DTO (ShoppingCartDTO)

| 字段 | 类型 | 说明 |
|------|------|------|
| dishId | Long | 菜品ID |
| setmealId | Long | 套餐ID |
| dishFlavor | String | 口味 |
| number | Integer | 数量 |

### 3.3 VO (ShoppingCartVO)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 购物车项ID |
| name | String | 商品名称 |
| image | String | 图片 |
| dishFlavor | String | 口味 |
| number | Integer | 数量 |
| amount | BigDecimal | 金额 |
| categoryId | Long | 分类ID |
| categoryName | String | 分类名称 |
| type | String | 商品类型：dish/setmeal |

## 4. 接口设计

### 4.1 添加商品到购物车

- **路径**: `POST /user/shoppingCart/add`
- **认证**: 需要用户登录
- **请求体**:
```json
{
  "dishId": 1,
  "setmealId": null,
  "dishFlavor": "辣度:微辣",
  "number": 1
}
```
- **响应**: `Result.success("添加成功")`

**业务逻辑**:
1. 验证商品存在且可售
2. 检查是否已存在相同商品（用户ID+菜品ID/套餐ID+口味）
3. 存在则数量累加，不存在则新增
4. 更新MySQL，删除Redis缓存

### 4.2 查看购物车列表

- **路径**: `GET /user/shoppingCart/list`
- **认证**: 需要用户登录
- **响应**: `Result<List<ShoppingCartVO>>`

**业务逻辑**:
1. 从Redis缓存获取，未命中查MySQL
2. 缓存未命中时回填Redis
3. 按分类展示购物车商品

### 4.3 修改购物车商品数量

- **路径**: `POST /user/shoppingCart/number`
- **认证**: 需要用户登录
- **请求体**:
```json
{
  "id": 1,
  "number": 2
}
```
- **响应**: `Result.success("修改成功")`

**业务逻辑**:
1. 数量为0时删除该记录
2. 数量>0时更新记录
3. 删除Redis缓存（懒更新）

### 4.4 删除购物车商品

- **路径**: `DELETE /user/shoppingCart`
- **认证**: 需要用户登录
- **请求体**:
```json
{
  "id": 1
}
```
- **响应**: `Result.success("删除成功")`

**业务逻辑**:
1. 根据ID删除MySQL记录
2. 删除Redis缓存

### 4.5 清空购物车

- **路径**: `DELETE /user/shoppingCart/clean`
- **认证**: 需要用户登录
- **响应**: `Result.success("清空成功")`

**业务逻辑**:
1. 删除当前用户所有购物车记录
2. 清空Redis缓存

## 5. 错误处理

| 场景 | 错误信息 |
|------|----------|
| 商品不存在 | 商品不存在 |
| 菜品已售罄 | 菜品已售罄 |
| 套餐已停售 | 套餐已停售 |
| 数量为0 | 自动移除 |

**降级策略**: Redis连接失败时降级到纯MySQL操作

## 6. 实现清单

| 序号 | 文件 | 说明 |
|------|------|------|
| 1 | sky-pojo/entity/ShoppingCart.java | 实体类 |
| 2 | sky-pojo/dto/ShoppingCartDTO.java | 请求DTO |
| 3 | sky-pojo/vo/ShoppingCartVO.java | 响应VO |
| 4 | sky-server/mapper/ShoppingCartMapper.java | Mapper接口 |
| 5 | sky-server/mapper/ShoppingCartMapper.xml | Mapper XML |
| 6 | sky-server/service/ShoppingCartService.java | 服务接口 |
| 7 | sky-server/service/impl/ShoppingCartServiceImpl.java | 服务实现 |
| 8 | sky-server/controller/user/ShoppingCartController.java | 控制器 |
| 9 | sky-server/src/test/java/.../ShoppingCartServiceImplTest.java | 单元测试 |

## 7. 测试计划

- 单元测试: ShoppingCartServiceImplTest
- 集成测试: ShoppingCartControllerTest
- 缓存测试: Redis缓存命中/未命中场景
