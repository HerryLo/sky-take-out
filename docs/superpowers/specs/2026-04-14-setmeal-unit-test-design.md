# Setmeal模块单元测试回归方案设计

## 1. 项目概述
为sky-take-out-back项目的Setmeal（套餐）模块设计单元测试回归方案，确保代码质量和功能稳定性。

## 2. 测试目标
- **测试覆盖率**：达到90%的代码覆盖率
- **测试类型**：单元测试
- **目标模块**：Setmeal模块（包括Service、Mapper、Controller层）
- **测试框架**：JUnit 5 + Mockito

## 3. 测试范围

### 3.1 Service层测试
测试SetmealServiceImpl的核心业务逻辑：
- `saveWithDish()` - 套餐保存功能
- `search()` - 套餐分页查询
- `getById()` - 根据ID查询套餐
- `getByCategoryId()` - 根据分类ID查询套餐
- `updateWithDish()` - 套餐更新功能
- `delete()` - 套餐删除功能
- `changeStatus()` - 状态变更功能

### 3.2 Mapper层测试
测试SetmealMapper的数据库操作：
- `save()` - 套餐保存
- `list()` - 套餐列表查询
- `getById()` - 根据ID查询
- `update()` - 套餐更新
- `deleteBySetmealId()` - 删除关联菜品
- `deleteById()` - 套餐删除

### 3.3 Controller层测试
测试SetmealController的所有API端点：
- 新增套餐：`POST /admin/setmeal`
- 分页查询：`GET /admin/setmeal/page`
- 根据ID查询：`GET /admin/setmeal/{id}`
- 根据分类ID查询：`GET /admin/setmeal/category/{categoryId}`
- 修改套餐：`PUT /admin/setmeal`
- 删除套餐：`DELETE /admin/setmeal/{id}`
- 启用/禁用：`POST /admin/setmeal/status/{status}`

## 4. 技术实现

### 4.1 依赖配置
需要添加以下Maven依赖：
```xml
<!-- Mockito for mocking -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.5.0</version>
    <scope>test</scope>
</dependency>

<!-- AssertJ for better assertions -->
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.24.2</version>
    <scope>test</scope>
</dependency>

<!-- JaCoCo for code coverage -->
<dependency>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
</dependency>
```

### 4.2 测试结构
```
src/test/java/com/liuheng/service/
├── impl/
│   └── SetmealServiceImplTest.java
├── mapper/
│   └── SetmealMapperTest.java
└── controller/
    └── admin/
        └── SetmealControllerTest.java
```

## 5. 测试覆盖率保证
- 使用JaCoCo生成详细的测试覆盖率报告
- 确保核心业务逻辑达到90%以上的代码覆盖率
- 覆盖所有主要方法和边界条件
- 定期运行测试并监控覆盖率变化

## 6. 测试执行策略
- 使用Maven命令运行测试：`mvn test -pl sky-server`
- 集成到CI/CD流程中自动执行
- 生成测试报告和覆盖率报告

## 7. 成功标准
- 所有测试用例通过
- 代码覆盖率达到90%以上
- 没有未覆盖的关键业务逻辑
- 测试结果可重复且稳定

## 8. 风险与缓解措施
- **风险**：测试用例编写耗时较长
  **缓解**：分阶段实施，先覆盖核心功能
- **风险**：Mock对象配置复杂
  **缓解**：使用Mockito简化模拟对象创建
- **风险**：测试环境与生产环境差异
  **缓解**：使用内存数据库和模拟数据

## 9. 后续工作
1. 创建详细的实现计划
2. 编写具体的测试用例
3. 配置测试环境和依赖
4. 运行测试并验证覆盖率
5. 集成到持续集成流程

---

**设计文档版本**：1.0  
**创建日期**：2026-04-14  
**设计师**：Claude Code