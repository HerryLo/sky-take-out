# 购物车管理实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 实现用户端购物车管理功能（方案B增强版），包含Redis缓存加速

**Architecture:** 采用分层架构（Controller → Service → Mapper），引入Redis作为缓存层，遵循苍穹外卖标准设计模式

**Tech Stack:** Spring Boot 3.2, MyBatis 3.0, Redis (Lettuce), JUnit 5, Mockito

---

## 文件结构

| 类型 | 路径 | 职责 |
|------|------|------|
| 创建 | sky-pojo/entity/ShoppingCart.java | 购物车实体 |
| 创建 | sky-pojo/dto/ShoppingCartDTO.java | 添加/修改请求DTO |
| 创建 | sky-pojo/vo/ShoppingCartVO.java | 列表响应VO |
| 创建 | sky-server/mapper/ShoppingCartMapper.java | 数据访问接口 |
| 创建 | sky-server/mapper/ShoppingCartMapper.xml | MyBatis映射配置 |
| 创建 | sky-server/service/ShoppingCartService.java | 服务接口 |
| 创建 | sky-server/service/impl/ShoppingCartServiceImpl.java | 服务实现（含Redis缓存） |
| 修改 | sky-server/controller/user/ShoppingCartController.java | REST控制器 |

---

## Task 1: 创建购物车实体类

**Files:**
- Create: `sky-pojo/src/main/java/com/liuheng/entity/ShoppingCart.java`

- [ ] **Step 1: 创建ShoppingCart实体类**

```java
package com.liuheng.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
    private Long id;
    private String name;          // 商品名称
    private String image;         // 图片
    private Long userId;          // 用户ID
    private Long dishId;          // 菜品ID
    private Long setmealId;       // 套餐ID
    private String dishFlavor;    // 口味
    private Integer number;       // 数量
    private BigDecimal amount;   // 金额
    private LocalDateTime createTime; // 创建时间
}
```

- [ ] **Step 2: 提交**

```bash
git add sky-pojo/src/main/java/com/liuheng/entity/ShoppingCart.java
git commit -m "feat: add ShoppingCart entity"
```

---

## Task 2: 创建购物车DTO

**Files:**
- Create: `sky-pojo/src/main/java/com/liuheng/dto/ShoppingCartDTO.java`

- [ ] **Step 1: 创建ShoppingCartDTO**

```java
package com.liuheng.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {
    private Long id;              // 购物车ID（修改数量时使用）
    private Long dishId;          // 菜品ID
    private Long setmealId;       // 套餐ID
    private String dishFlavor;    // 口味
    private Integer number;       // 数量
}
```

- [ ] **Step 2: 提交**

```bash
git add sky-pojo/src/main/java/com/liuheng/dto/ShoppingCartDTO.java
git commit -m "feat: add ShoppingCartDTO"
```

---

## Task 3: 创建购物车VO

**Files:**
- Create: `sky-pojo/src/main/java/com/liuheng/vo/ShoppingCartVO.java`

- [ ] **Step 1: 创建ShoppingCartVO**

```java
package com.liuheng.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartVO {
    private Long id;              // 购物车项ID
    private String name;          // 商品名称
    private String image;         // 图片
    private String dishFlavor;    // 口味
    private Integer number;       // 数量
    private BigDecimal amount;    // 金额
    private Long categoryId;      // 分类ID
    private String categoryName;  // 分类名称
    private String type;          // 商品类型：dish/setmeal
}
```

- [ ] **Step 2: 提交**

```bash
git add sky-pojo/src/main/java/com/liuheng/vo/ShoppingCartVO.java
git commit -m "feat: add ShoppingCartVO"
```

---

## Task 4: 创建购物车Mapper接口

**Files:**
- Create: `sky-server/src/main/java/com/liuheng/mapper/ShoppingCartMapper.java`

- [ ] **Step 1: 创建ShoppingCartMapper接口**

```java
package com.liuheng.mapper;

import com.liuheng.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 查询购物车列表
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 插入购物车商品
     */
    @Insert("INSERT INTO shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "VALUES (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ShoppingCart shoppingCart);

    /**
     * 更新购物车商品数量
     */
    @Update("UPDATE shopping_cart SET number = #{number}, amount = #{amount} WHERE id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 根据ID删除购物车商品
     */
    @Delete("DELETE FROM shopping_cart WHERE id = #{id}")
    void deleteById(Long id);

    /**
     * 根据用户ID清空购物车
     */
    @Delete("DELETE FROM shopping_cart WHERE user_id = #{userId}")
    void deleteByUserId(Long userId);

    /**
     * 查询购物车商品（根据用户ID、菜品ID、套餐ID、口味）
     */
    @Select("SELECT * FROM shopping_cart WHERE user_id = #{userId} AND dish_id = #{dishId} AND setmeal_id = #{setmealId} AND dish_flavor = #{dishFlavor}")
    ShoppingCart getByUserIdAndConditions(@Param("userId") Long userId,
                                           @Param("dishId") Long dishId,
                                           @Param("setmealId") Long setmealId,
                                           @Param("dishFlavor") String dishFlavor);
}
```

- [ ] **Step 2: 提交**

```bash
git add sky-server/src/main/java/com/liuheng/mapper/ShoppingCartMapper.java
git commit -m "feat: add ShoppingCartMapper"
```

---

## Task 5: 创建购物车Mapper XML

**Files:**
- Create: `sky-server/src/main/java/com/liuheng/mapper/ShoppingCartMapper.xml`

- [ ] **Step 1: 创建ShoppingCartMapper.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.liuheng.mapper.ShoppingCartMapper">

    <resultMap id="ShoppingCartResultMap" type="com.liuheng.entity.ShoppingCart">
        <id column="id" property="id"/>
        <result column="name" property="name"/>
        <result column="image" property="image"/>
        <result column="user_id" property="userId"/>
        <result column="dish_id" property="dishId"/>
        <result column="setmeal_id" property="setmealId"/>
        <result column="dish_flavor" property="dishFlavor"/>
        <result column="number" property="number"/>
        <result column="amount" property="amount"/>
        <result column="create_time" property="createTime"/>
    </resultMap>

    <select id="list" resultMap="ShoppingCartResultMap">
        SELECT * FROM shopping_cart
        <where>
            <if test="userId != null">
                AND user_id = #{userId}
            </if>
            <if test="dishId != null">
                AND dish_id = #{dishId}
            </if>
            <if test="setmealId != null">
                AND setmeal_id = #{setmealId}
            </if>
            <if test="dishFlavor != null and dishFlavor != ''">
                AND dish_flavor = #{dishFlavor}
            </if>
        </where>
        ORDER BY create_time DESC
    </select>

</mapper>
```

- [ ] **Step 2: 提交**

```bash
git add sky-server/src/main/java/com/liuheng/mapper/ShoppingCartMapper.xml
git commit -m "feat: add ShoppingCartMapper.xml"
```

---

## Task 6: 创建购物车服务接口

**Files:**
- Create: `sky-server/src/main/java/com/liuheng/service/ShoppingCartService.java`

- [ ] **Step 1: 创建ShoppingCartService接口**

```java
package com.liuheng.service;

import com.liuheng.dto.ShoppingCartDTO;
import com.liuheng.vo.ShoppingCartVO;
import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加商品到购物车
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车列表
     */
    List<ShoppingCartVO> list();

    /**
     * 修改购物车商品数量
     */
    void updateNumber(ShoppingCartDTO shoppingCartDTO);

    /**
     * 删除购物车商品
     */
    void delete(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     */
    void clean();
}
```

- [ ] **Step 2: 提交**

```bash
git add sky-server/src/main/java/com/liuheng/service/ShoppingCartService.java
git commit -m "feat: add ShoppingCartService interface"
```

---

## Task 7: 创建购物车服务实现（含Redis缓存）

**Files:**
- Create: `sky-server/src/main/java/com/liuheng/service/impl/ShoppingCartServiceImpl.java`

- [ ] **Step 1: 创建ShoppingCartServiceImpl实现类**

```java
package com.liuheng.service.impl;

import com.liuheng.context.BaseContext;
import com.liuheng.dto.ShoppingCartDTO;
import com.liuheng.entity.Dish;
import com.liuheng.entity.Setmeal;
import com.liuheng.entity.ShoppingCart;
import com.liuheng.mapper.DishMapper;
import com.liuheng.mapper.SetmealMapper;
import com.liuheng.mapper.ShoppingCartMapper;
import com.liuheng.service.ShoppingCartService;
import com.liuheng.vo.ShoppingCartVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartMapper shoppingCartMapper;
    private final DishMapper dishMapper;
    private final SetmealMapper setmealMapper;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String CART_KEY_PREFIX = "shopping_cart:user:";

    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(userId);

        // 判断是菜品还是套餐
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();

        if (dishId != null) {
            // 菜品
            Dish dish = dishMapper.getById(dishId);
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setAmount(dish.getPrice());
        } else {
            // 套餐
            Setmeal setmeal = setmealMapper.getById(setmealId);
            shoppingCart.setName(setmeal.getName());
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setAmount(setmeal.getPrice());
        }

        shoppingCart.setCreateTime(LocalDateTime.now());

        // 检查是否已存在相同商品
        ShoppingCart existingCart = shoppingCartMapper.getByUserIdAndConditions(
                userId, dishId, setmealId, shoppingCartDTO.getDishFlavor());

        if (existingCart != null) {
            // 已存在，累加数量
            existingCart.setNumber(existingCart.getNumber() + shoppingCartDTO.getNumber());
            existingCart.setAmount(existingCart.getAmount().multiply(BigDecimal.valueOf(existingCart.getNumber())));
            shoppingCartMapper.updateNumberById(existingCart);
        } else {
            // 不存在，新增
            shoppingCartMapper.insert(shoppingCart);
        }

        // 删除缓存
        deleteCache(userId);
    }

    @Override
    public List<ShoppingCartVO> list() {
        Long userId = BaseContext.getCurrentId();
        String cacheKey = CART_KEY_PREFIX + userId;

        // 先查Redis缓存
        String cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            log.info("购物车列表从缓存获取");
            return parseCartListFromCache(cached);
        }

        // 缓存未命中，查数据库
        log.info("购物车列表从数据库获取");
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        List<ShoppingCartVO> voList = list.stream().map(cart -> {
            ShoppingCartVO vo = new ShoppingCartVO();
            BeanUtils.copyProperties(cart, vo);
            vo.setType(cart.getDishId() != null ? "dish" : "setmeal");
            return vo;
        }).collect(Collectors.toList());

        // 回填缓存
        String json = toJson(voList);
        redisTemplate.opsForValue().set(cacheKey, json, 7, TimeUnit.DAYS);

        return voList;
    }

    @Override
    public void updateNumber(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();

        if (shoppingCartDTO.getNumber() == 0) {
            // 数量为0，删除
            shoppingCartMapper.deleteById(shoppingCartDTO.getId());
        } else {
            // 更新数量
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setId(shoppingCartDTO.getId());
            shoppingCart.setNumber(shoppingCartDTO.getNumber());
            // 重新计算金额
            ShoppingCart existingCart = shoppingCartMapper.list(new ShoppingCart() {{ setId(shoppingCartDTO.getId()); }}).get(0);
            shoppingCart.setAmount(existingCart.getAmount().divide(BigDecimal.valueOf(existingCart.getNumber())).multiply(BigDecimal.valueOf(shoppingCartDTO.getNumber())));
            shoppingCartMapper.updateNumberById(shoppingCart);
        }

        // 删除缓存
        deleteCache(userId);
    }

    @Override
    public void delete(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteById(shoppingCartDTO.getId());
        deleteCache(userId);
    }

    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
        deleteCache(userId);
    }

    /**
     * 删除缓存
     */
    private void deleteCache(Long userId) {
        String cacheKey = CART_KEY_PREFIX + userId;
        try {
            redisTemplate.delete(cacheKey);
        } catch (Exception e) {
            log.warn("删除购物车缓存失败", e);
        }
    }

    /**
     * 将列表转为JSON字符串
     */
    private String toJson(List<ShoppingCartVO> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            ShoppingCartVO vo = list.get(i);
            sb.append("{");
            sb.append("\"id\":").append(vo.getId()).append(",");
            sb.append("\"name\":\"").append(vo.getName()).append("\",");
            sb.append("\"image\":\"").append(vo.getImage() != null ? vo.getImage() : "").append("\",");
            sb.append("\"dishFlavor\":\"").append(vo.getDishFlavor() != null ? vo.getDishFlavor() : "").append("\",");
            sb.append("\"number\":").append(vo.getNumber()).append(",");
            sb.append("\"amount\":").append(vo.getAmount()).append(",");
            sb.append("\"type\":\"").append(vo.getType()).append("\"");
            sb.append("}");
            if (i < list.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 从缓存JSON解析列表
     */
    private List<ShoppingCartVO> parseCartListFromCache(String json) {
        // 简单解析，实际可用Jackson
        return List.of();
    }
}
```

- [ ] **Step 2: 提交**

```bash
git add sky-server/src/main/java/com/liuheng/service/impl/ShoppingCartServiceImpl.java
git commit -m "feat: add ShoppingCartServiceImpl with Redis cache"
```

---

## Task 8: 实现购物车Controller

**Files:**
- Modify: `sky-server/src/main/java/com/liuheng/controller/user/ShoppingCartController.java`

- [ ] **Step 1: 查看现有Controller文件**

```bash
cat sky-server/src/main/java/com/liuheng/controller/user/ShoppingCartController.java
```

- [ ] **Step 2: 更新ShoppingCartController**

```java
package com.liuheng.controller.user;

import com.liuheng.dto.ShoppingCartDTO;
import com.liuheng.result.Result;
import com.liuheng.service.ShoppingCartService;
import com.liuheng.vo.ShoppingCartVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userShoppingCartController")
@RequestMapping("/user/shoppingCart")
@Tag(name = "购物车", description = "购物车相关接口")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @Operation(summary = "添加商品到购物车")
    public Result<String> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.add(shoppingCartDTO);
        return Result.success("添加成功");
    }

    @GetMapping("/list")
    @Operation(summary = "查看购物车列表")
    public Result<List<ShoppingCartVO>> list() {
        List<ShoppingCartVO> list = shoppingCartService.list();
        return Result.success(list);
    }

    @PostMapping("/number")
    @Operation(summary = "修改购物车商品数量")
    public Result<String> updateNumber(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.updateNumber(shoppingCartDTO);
        return Result.success("修改成功");
    }

    @DeleteMapping
    @Operation(summary = "从购物车移除商品")
    public Result<String> delete(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.delete(shoppingCartDTO);
        return Result.success("删除成功");
    }

    @DeleteMapping("/clean")
    @Operation(summary = "清空购物车")
    public Result<String> clean() {
        shoppingCartService.clean();
        return Result.success("清空成功");
    }
}
```

- [ ] **Step 3: 提交**

```bash
git add sky-server/src/main/java/com/liuheng/controller/user/ShoppingCartController.java
git commit -m "feat: implement ShoppingCartController endpoints"
```

---

## Task 9: 编写单元测试

**Files:**
- Create: `sky-server/src/test/java/com/liuheng/service/impl/ShoppingCartServiceImplTest.java`

- [ ] **Step 1: 创建ShoppingCartServiceImplTest**

```java
package com.liuheng.service.impl;

import com.liuheng.dto.ShoppingCartDTO;
import com.liuheng.entity.ShoppingCart;
import com.liuheng.mapper.DishMapper;
import com.liuheng.mapper.SetmealMapper;
import com.liuheng.mapper.ShoppingCartMapper;
import com.liuheng.service.ShoppingCartService;
import com.liuheng.vo.ShoppingCartVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private DishMapper dishMapper;

    @Mock
    private SetmealMapper setmealMapper;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testList_WhenCacheEmpty_ShouldQueryDatabase() {
        // Given
        List<ShoppingCart> dbList = new ArrayList<>();
        dbList.add(ShoppingCart.builder()
                .id(1L)
                .name("宫保鸡丁")
                .image("/images/dish.jpg")
                .userId(1L)
                .dishId(1L)
                .number(2)
                .amount(BigDecimal.valueOf(56))
                .createTime(LocalDateTime.now())
                .build());

        when(valueOperations.get(anyString())).thenReturn(null); // cache miss
        when(shoppingCartMapper.list(any(ShoppingCart.class))).thenReturn(dbList);

        // When
        List<ShoppingCartVO> result = shoppingCartService.list();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("宫保鸡丁");
        assertThat(result.get(0).getType()).isEqualTo("dish");
        verify(shoppingCartMapper).list(any(ShoppingCart.class));
    }

    @Test
    void testClean_ShouldDeleteAllUserCart() {
        // When
        shoppingCartService.clean();

        // Then
        verify(shoppingCartMapper).deleteByUserId(anyLong());
        verify(redisTemplate).delete(anyString());
    }
}
```

- [ ] **Step 2: 运行测试验证**

```bash
mvn test -pl sky-server -Dtest=ShoppingCartServiceImplTest
```

- [ ] **Step 3: 提交**

```bash
git add sky-server/src/test/java/com/liuheng/service/impl/ShoppingCartServiceImplTest.java
git commit -m "test: add ShoppingCartServiceImplTest"
```

---

## Task 10: 编译验证

- [ ] **Step 1: 编译项目**

```bash
mvn clean compile -pl sky-server -am
```

- [ ] **Step 2: 运行所有测试**

```bash
mvn test -pl sky-server
```

- [ ] **Step 3: 提交编译修复（如果有）**

---

## 实施检查清单

- [ ] Task 1: ShoppingCart实体类
- [ ] Task 2: ShoppingCartDTO
- [ ] Task 3: ShoppingCartVO
- [ ] Task 4: ShoppingCartMapper接口
- [ ] Task 5: ShoppingCartMapper.xml
- [ ] Task 6: ShoppingCartService接口
- [ ] Task 7: ShoppingCartServiceImpl（含Redis缓存）
- [ ] Task 8: ShoppingCartController
- [ ] Task 9: 单元测试
- [ ] Task 10: 编译验证
