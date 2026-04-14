# Setmeal模块单元测试实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 为Setmeal模块创建完整的单元测试，达到90%的代码覆盖率

**Architecture:** 使用JUnit 5 + Mockito进行单元测试，分层测试Service、Mapper和Controller层

**Tech Stack:** JUnit 5, Mockito, AssertJ, JaCoCo

---

## 任务1: 配置测试依赖

**Files:**
- Modify: `pom.xml`

- [ ] **步骤1: 添加Mockito依赖**
```xml
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.5.0</version>
    <scope>test</scope>
</dependency>
```

- [ ] **步骤2: 添加AssertJ依赖**
```xml
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.24.2</version>
    <scope>test</scope>
</dependency>
```

- [ ] **步骤3: 添加JaCoCo插件**
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

- [ ] **步骤4: 运行Maven更新**
```bash
mvn clean install
```

- [ ] **步骤5: 提交依赖配置**
```bash
git add pom.xml
git commit -m "feat: add test dependencies for setmeal unit tests"
```

---

## 任务2: 创建SetmealService测试类

**Files:**
- Create: `sky-server/src/test/java/com/liuheng/service/impl/SetmealServiceImplTest.java`

- [ ] **步骤1: 创建测试类框架**
```java
package com.liuheng.service.impl;

import com.liuheng.dto.SetmealDTO;
import com.liuheng.entity.Setmeal;
import com.liuheng.entity.SetmealDish;
import com.liuheng.mapper.CategoryMapper;
import com.liuheng.mapper.SetmealDishMapper;
import com.liuheng.mapper.SetmealMapper;
import com.liuheng.result.PageResult;
import com.liuheng.vo.SetmealVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SetmealServiceImplTest {

    @Mock
    private SetmealMapper setmealMapper;

    @Mock
    private SetmealDishMapper setmealDishMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private SetmealServiceImpl setmealService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}
```

- [ ] **步骤2: 测试saveWithDish方法**
```java
@Test
void testSaveWithDish() {
    // Given
    SetmealDTO setmealDTO = new SetmealDTO();
    setmealDTO.setName("测试套餐");
    setmealDTO.setCategoryId(1L);
    setmealDTO.setPrice(99.99);
    setmealDTO.setStatus(1);
    
    List<SetmealDish> dishes = new ArrayList<>();
    SetmealDish dish = new SetmealDish();
    dish.setDishId(1L);
    dish.setDishName("测试菜品");
    dish.setPrice(19.99);
    dishes.add(dish);
    setmealDTO.setSetmealDishes(dishes);

    when(setmealMapper.save(any(Setmeal.class))).thenReturn(1L);
    when(setmealDishMapper.save(anyList())).thenReturn(1);

    // When
    boolean result = setmealService.saveWithDish(setmealDTO);

    // Then
    assertTrue(result);
    verify(setmealMapper, times(1)).save(any(Setmeal.class));
    verify(setmealDishMapper, times(1)).save(anyList());
}
```

- [ ] **步骤3: 测试search方法**
```java
@Test
void testSearch() {
    // Given
    SetmealPageQueryDTO queryDTO = new SetmealPageQueryDTO();
    queryDTO.setPage(1);
    queryDTO.setPageSize(10);
    queryDTO.setName("测试");
    queryDTO.setCategoryId(1);
    queryDTO.setStatus(1);

    List<Setmeal> setmealList = new ArrayList<>();
    Setmeal setmeal = new Setmeal();
    setmeal.setId(1L);
    setmeal.setName("测试套餐");
    setmeal.setCategoryId(1L);
    setmeal.setStatus(1);
    setmealList.add(setmeal);

    when(setmealMapper.list(any(SetmealPageQueryDTO.class))).thenReturn(setmealList);
    when(categoryMapper.getById(1L)).thenReturn(new Category(1L, "测试分类"));
    when(setmealDishMapper.getBySetmealId(1L)).thenReturn(new ArrayList<>());

    // When
    PageResult result = setmealService.search(queryDTO);

    // Then
    assertNotNull(result);
    assertEquals(1, result.getTotal());
    assertEquals(1, result.getRecords().size());
}
```

- [ ] **步骤4: 测试getById方法**
```java
@Test
void testGetById() {
    // Given
    Long setId = 1L;
    Setmeal setmeal = new Setmeal();
    setmeal.setId(setId);
    setmeal.setName("测试套餐");
    setmeal.setCategoryId(1L);
    setmeal.setStatus(1);

    when(setmealMapper.getById(setId)).thenReturn(setmeal);
    when(categoryMapper.getById(1L)).thenReturn(new Category(1L, "测试分类"));
    when(setmealDishMapper.getBySetmealId(setId)).thenReturn(new ArrayList<>());

    // When
    SetmealVO result = setmealService.getById(setId);

    // Then
    assertNotNull(result);
    assertEquals(setId, result.getId());
    assertEquals("测试套餐", result.getName());
}
```

- [ ] **步骤5: 测试getByCategoryId方法**
```java
@Test
void testGetByCategoryId() {
    // Given
    Long categoryId = 1L;
    SetmealPageQueryDTO queryDTO = new SetmealPageQueryDTO();
    queryDTO.setCategoryId(categoryId.intValue());
    queryDTO.setStatus(1);

    List<Setmeal> setmealList = new ArrayList<>();
    Setmeal setmeal = new Setmeal();
    setmeal.setId(1L);
    setmeal.setName("测试套餐");
    setmeal.setCategoryId(categoryId);
    setmeal.setStatus(1);
    setmealList.add(setmeal);

    when(setmealMapper.list(queryDTO)).thenReturn(setmealList);
    when(categoryMapper.getById(categoryId)).thenReturn(new Category(categoryId, "测试分类"));

    // When
    List<SetmealVO> result = setmealService.getByCategoryId(categoryId);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(categoryId, result.get(0).getCategoryId());
}
```

- [ ] **步骤6: 测试updateWithDish方法**
```java
@Test
void testUpdateWithDish() {
    // Given
    SetmealDTO setmealDTO = new SetmealDTO();
    setmealDTO.setId(1L);
    setmealDTO.setName("更新套餐");
    setmealDTO.setCategoryId(1L);
    setmealDTO.setPrice(129.99);
    setmealDTO.setStatus(1);
    
    List<SetmealDish> dishes = new ArrayList<>();
    SetmealDish dish = new SetmealDish();
    dish.setDishId(1L);
    dish.setDishName("更新菜品");
    dishes.add(dish);
    setmealDTO.setSetmealDishes(dishes);

    when(setmealMapper.getById(1L)).thenReturn(new Setmeal(1L, "旧套餐", 1L, 99.99, 1));
    when(setmealMapper.update(any(Setmeal.class))).thenReturn(1);
    when(setmealDishMapper.deleteBySetmealId(1L)).thenReturn(1);
    when(setmealDishMapper.save(anyList())).thenReturn(1);

    // When
    boolean result = setmealService.updateWithDish(setmealDTO);

    // Then
    assertTrue(result);
    verify(setmealMapper, times(1)).update(any(Setmeal.class));
    verify(setmealDishMapper, times(1)).deleteBySetmealId(1L);
    verify(setmealDishMapper, times(1)).save(anyList());
}
```

- [ ] **步骤7: 测试delete方法**
```java
@Test
void testDelete() {
    // Given
    Long setId = 1L;
    when(setmealMapper.getById(setId)).thenReturn(new Setmeal(setId, "测试套餐", 1L, 99.99, 1));
    when(setmealDishMapper.deleteBySetmealId(setId)).thenReturn(1);
    when(setmealMapper.deleteById(setId)).thenReturn(1);

    // When
    boolean result = setmealService.delete(setId);

    // Then
    assertTrue(result);
    verify(setmealMapper, times(1)).getById(setId);
    verify(setmealDishMapper, times(1)).deleteBySetmealId(setId);
    verify(setmealMapper, times(1)).deleteById(setId);
}
```

- [ ] **步骤8: 测试changeStatus方法**
```java
@Test
void testChangeStatus() {
    // Given
    Long setId = 1L;
    Integer status = 0;
    when(setmealMapper.getById(setId)).thenReturn(new Setmeal(setId, "测试套餐", 1L, 99.99, 1));
    when(setmealMapper.update(any(Setmeal.class))).thenReturn(1);

    // When
    boolean result = setmealService.changeStatus(status, setId);

    // Then
    assertTrue(result);
    verify(setmealMapper, times(1)).getById(setId);
    verify(setmealMapper, times(1)).update(any(Setmeal.class));
}
```

- [ ] **步骤9: 运行测试**
```bash
mvn test -pl sky-server -Dtest=SetmealServiceImplTest
```

- [ ] **步骤10: 提交测试类**
```bash
git add sky-server/src/test/java/com/liuheng/service/impl/SetmealServiceImplTest.java
git commit -m "feat: add SetmealServiceImpl unit tests"
```

---

## 任务3: 创建SetmealMapper测试类

**Files:**
- Create: `sky-server/src/test/java/com/liuheng/mapper/SetmealMapperTest.java`

- [ ] **步骤1: 创建测试类框架**
```java
package com.liuheng.mapper;

import com.liuheng.dto.SetmealPageQueryDTO;
import com.liuheng.entity.Setmeal;
import com.liuheng.entity.SetmealDish;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SetmealMapperTest {

    @Autowired
    private SetmealMapper setmealMapper;

    @Test
    void testSave() {
        // TODO: 实现save方法测试
    }

    @Test
    void testList() {
        // TODO: 实现list方法测试
    }

    @Test
    void testGetById() {
        // TODO: 实现getById方法测试
    }

    @Test
    void testUpdate() {
        // TODO: 实现update方法测试
    }

    @Test
    void testDeleteBySetmealId() {
        // TODO: 实现deleteBySetmealId方法测试
    }

    @Test
    void testDeleteById() {
        // TODO: 实现deleteById方法测试
    }
}
```

- [ ] **步骤2: 实现save方法测试**
```java
@Test
void testSave() {
    // Given
    Setmeal setmeal = new Setmeal();
    setmeal.setCategoryId(1L);
    setmeal.setName("测试套餐");
    setmeal.setPrice(99.99);
    setmeal.setStatus(1);
    setmeal.setDescription("测试描述");
    setmeal.setImage("test.jpg");

    // When
    int result = setmealMapper.save(setmeal);

    // Then
    assertEquals(1, result);
    assertNotNull(setmeal.getId());
}
```

- [ ] **步骤3: 实现list方法测试**
```java
@Test
void testList() {
    // Given
    SetmealPageQueryDTO queryDTO = new SetmealPageQueryDTO();
    queryDTO.setName("测试");
    queryDTO.setCategoryId(1);
    queryDTO.setStatus(1);

    // When
    List<Setmeal> result = setmealMapper.list(queryDTO);

    // Then
    assertNotNull(result);
    assertTrue(result.size() > 0);
}
```

- [ ] **步骤4: 实现getById方法测试**
```java
@Test
void testGetById() {
    // Given
    Long setId = 1L;

    // When
    Setmeal result = setmealMapper.getById(setId);

    // Then
    assertNotNull(result);
    assertEquals(setId, result.getId());
}
```

- [ ] **步骤5: 实现update方法测试**
```java
@Test
void testUpdate() {
    // Given
    Setmeal setmeal = new Setmeal();
    setmeal.setId(1L);
    setmeal.setName("更新套餐");
    setmeal.setPrice(129.99);
    setmeal.setStatus(0);

    // When
    int result = setmealMapper.update(setmeal);

    // Then
    assertEquals(1, result);
}
```

- [ ] **步骤6: 实现deleteBySetmealId方法测试**
```java
@Test
void testDeleteBySetmealId() {
    // Given
    Long setId = 1L;

    // When
    int result = setmealMapper.deleteBySetmealId(setId);

    // Then
    assertEquals(1, result);
}
```

- [ ] **步骤7: 实现deleteById方法测试**
```java
@Test
void testDeleteById() {
    // Given
    Long setId = 1L;

    // When
    int result = setmealMapper.deleteById(setId);

    // Then
    assertEquals(1, result);
}
```

- [ ] **步骤8: 运行测试**
```bash
mvn test -pl sky-server -Dtest=SetmealMapperTest
```

- [ ] **步骤9: 提交测试类**
```bash
git add sky-server/src/test/java/com/liuheng/mapper/SetmealMapperTest.java
git commit -m "feat: add SetmealMapper unit tests"
```

---

## 任务4: 创建SetmealController测试类

**Files:**
- Create: `sky-server/src/test/java/com/liuheng/controller/admin/SetmealControllerTest.java`

- [ ] **步骤1: 创建测试类框架**
```java
package com.liuheng.controller.admin;

import com.liuheng.dto.SetmealDTO;
import com.liuheng.result.Result;
import com.liuheng.service.SetmealService;
import com.liuheng.vo.SetmealVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SetmealControllerTest {

    @Mock
    private SetmealService setmealService;

    @InjectMocks
    private SetmealController setmealController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}
```

- [ ] **步骤2: 测试save方法**
```java
@Test
void testSave() {
    // Given
    SetmealDTO setmealDTO = new SetmealDTO();
    setmealDTO.setName("测试套餐");
    setmealDTO.setCategoryId(1L);
    setmealDTO.setPrice(99.99);
    setmealDTO.setStatus(1);
    
    List<SetmealDish> dishes = new ArrayList<>();
    SetmealDish dish = new SetmealDish();
    dish.setDishId(1L);
    dishes.add(dish);
    setmealDTO.setSetmealDishes(dishes);

    when(setmealService.saveWithDish(any(SetmealDTO.class))).thenReturn(true);

    // When
    ResponseEntity<Result<Boolean>> response = setmealController.save(setmealDTO);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getData());
    verify(setmealService, times(1)).saveWithDish(any(SetmealDTO.class));
}
```

- [ ] **步骤3: 测试page方法**
```java
@Test
void testPage() {
    // Given
    SetmealPageQueryDTO queryDTO = new SetmealPageQueryDTO();
    queryDTO.setPage(1);
    queryDTO.setPageSize(10);

    PageResult<SetmealVO> pageResult = new PageResult<>();
    pageResult.setTotal(1);
    pageResult.setRecords(new ArrayList<>());

    when(setmealService.search(queryDTO)).thenReturn(pageResult);

    // When
    ResponseEntity<Result<PageResult<SetmealVO>>> response = setmealController.page(queryDTO);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody().getData());
    assertEquals(1, response.getBody().getData().getTotal());
}
```

- [ ] **步骤4: 测试getById方法**
```java
@Test
void testGetById() {
    // Given
    Long setId = 1L;
    SetmealVO setmealVO = new SetmealVO();
    setmealVO.setId(setId);
    setmealVO.setName("测试套餐");

    when(setmealService.getById(setId)).thenReturn(setmealVO);

    // When
    ResponseEntity<Result<SetmealVO>> response = setmealController.getById(setId);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody().getData());
    assertEquals(setId, response.getBody().getData().getId());
}
```

- [ ] **步骤5: 测试getByCategoryId方法**
```java
@Test
void testGetByCategoryId() {
    // Given
    Long categoryId = 1L;
    List<SetmealVO> setmealVOList = new ArrayList<>();
    SetmealVO setmealVO = new SetmealVO();
    setmealVO.setId(1L);
    setmealVO.setName("测试套餐");
    setmealVOList.add(setmealVO);

    when(setmealService.getByCategoryId(categoryId)).thenReturn(setmealVOList);

    // When
    ResponseEntity<Result<List<SetmealVO>>> response = setmealController.getByCategoryId(categoryId);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody().getData());
    assertEquals(1, response.getBody().getData().size());
}
```

- [ ] **步骤6: 测试update方法**
```java
@Test
void testUpdate() {
    // Given
    SetmealDTO setmealDTO = new SetmealDTO();
    setmealDTO.setId(1L);
    setmealDTO.setName("更新套餐");

    when(setmealService.updateWithDish(any(SetmealDTO.class))).thenReturn(true);

    // When
    ResponseEntity<Result<Boolean>> response = setmealController.update(setmealDTO);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getData());
    verify(setmealService, times(1)).updateWithDish(any(SetmealDTO.class));
}
```

- [ ] **步骤7: 测试delete方法**
```java
@Test
void testDelete() {
    // Given
    Long setId = 1L;
    when(setmealService.delete(setId)).thenReturn(true);

    // When
    ResponseEntity<Result<Boolean>> response = setmealController.delete(setId);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getData());
    verify(setmealService, times(1)).delete(setId);
}
```

- [ ] **步骤8: 测试changeStatus方法**
```java
@Test
void testChangeStatus() {
    // Given
    Long setId = 1L;
    Integer status = 0;
    when(setmealService.changeStatus(status, setId)).thenReturn(true);

    // When
    ResponseEntity<Result<Boolean>> response = setmealController.changeStatus(status, setId);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(response.getBody().getData());
    verify(setmealService, times(1)).changeStatus(status, setId);
}
```

- [ ] **步骤9: 运行测试**
```bash
mvn test -pl sky-server -Dtest=SetmealControllerTest
```

- [ ] **步骤10: 提交测试类**
```bash
git add sky-server/src/test/java/com/liuheng/controller/admin/SetmealControllerTest.java
git commit -m "feat: add SetmealController unit tests"
```

---

## 任务5: 运行完整测试套件并检查覆盖率

- [ ] **步骤1: 运行所有Setmeal相关测试**
```bash
mvn test -pl sky-server -Dtest=Setmeal*
```

- [ ] **步骤2: 生成JaCoCo覆盖率报告**
```bash
mvn jacoco:report
```

- [ ] **步骤3: 检查覆盖率报告**
```bash
open target/site/jacoco/index.html
```

- [ ] **步骤4: 确保达到90%覆盖率目标**
- [ ] **步骤5: 提交测试结果和覆盖率报告**

---

## 任务6: 集成到CI/CD流程

- [ ] **步骤1: 配置Maven Surefire插件**
- [ ] **步骤2: 添加测试阶段到构建流程**
- [ ] **步骤3: 配置代码覆盖率报告生成**
- [ ] **步骤4: 提交CI/CD配置变更**

---

计划完成并保存到 `docs/superpowers/plans/2026-04-14-setmeal-unit-test-implementation-plan.md`。两个执行选项：

**1. Subagent-Driven (recommended)** - 我派遣一个全新的子代理处理每个任务，任务间进行审查，快速迭代

**2. Inline Execution** - 在此会话中使用executing-plans执行任务，带有检查点的批量执行

**哪种方法？**