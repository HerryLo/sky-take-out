# SetmealController Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement remaining SetmealController functionality in two phases: query methods first, then update/delete operations.

**Architecture:** Spring Boot REST controller with service layer pattern using MyBatis mappers. Two-phase implementation ensures query foundation before adding complex update/delete operations.

**Tech Stack:** Spring Boot 3.2.12, MyBatis, MySQL, PageHelper for pagination, JWT authentication

---

## Phase 1: Query Methods Implementation

### Task 1: Create SetmealVO Class

**Files:**
- Create: `sky-pojo/src/main/java/com/liuheng/vo/SetmealVO.java`

- [ ] **Step 1: Create SetmealVO with required fields**

```java
package com.liuheng.vo;

import lombok.Data;
import com.liuheng.entity.SetmealDish;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SetmealVO {
    private Long id;
    private Long categoryId;
    private String name;
    private BigDecimal price;
    private Integer status;
    private String description;
    private String image;
    private LocalDateTime createTime;
    private String categoryName; // 关联分类名称
    private List<SetmealDish> setmealDishes; // 关联菜品
}
```

- [ ] **Step 2: Verify Lombok dependency**

Check `sky-common/pom.xml` has Lombok dependency
Expected: Lombok should be available in common module

- [ ] **Step 3: Test compilation**

Run: `mvn clean compile -pl sky-pojo`
Expected: SUCCESS with no compilation errors

- [ ] **Step 4: Commit**

```bash
git add sky-pojo/src/main/java/com/liuheng/vo/SetmealVO.java
git commit -m "feat: add SetmealVO class for detailed setmeal responses"
```

### Task 2: Enhance SetmealService Interface

**Files:**
- Modify: `sky-server/src/main/java/com/liuheng/service/SetmealService.java`

- [ ] **Step 1: Add query methods to interface**

```java
package com.liuheng.service;

import com.liuheng.dto.SetmealDTO;
import com.liuheng.dto.SetmealPageQueryDTO;
import com.liuheng.vo.SetmealVO;
import com.liuheng.result.PageResult;

public interface SetmealService {
    boolean saveWithDish(SetmealDTO setmealDTO);
    
    // Phase 1 methods
    PageResult<SetmealVO> search(SetmealPageQueryDTO setmealPageQueryDTO);
    SetmealVO getById(Long id);
    List<SetmealVO> getByCategoryId(Long categoryId);
}
```

- [ ] **Step 2: Check PageResult import**

Verify PageResult import: `import com.liuheng.result.PageResult;`
If missing, add the import statement

- [ ] **Step 3: Test compilation**

Run: `mvn clean compile -pl sky-server`
Expected: SUCCESS with no compilation errors

- [ ] **Step 4: Commit**

```bash
git add sky-server/src/main/java/com/liuheng/service/SetmealService.java
git commit -m "feat: add query methods to SetmealService interface"
```

### Task 3: Implement search() Method in Service Layer

**Files:**
- Create: `sky-server/src/main/java/com/liuheng/service/impl/SetmealServiceImpl.java`
- Modify: `sky-server/src/main/java/com/liuheng/mapper/SetmealMapper.java` (add method)
- Add: Required dependency injection in SetmealServiceImpl

- [ ] **Step 1: Create SetmealServiceImpl class**

```java
package com.liuheng.service.impl;

import com.liuheng.dto.SetmealDTO;
import com.liuheng.dto.SetmealPageQueryDTO;
import com.liuheng.entity.Category;
import com.liuheng.entity.Setmeal;
import com.liuheng.entity.SetmealDish;
import com.liuheng.mapper.CategoryMapper;
import com.liuheng.mapper.SetmealDishMapper;
import com.liuheng.mapper.SetmealMapper;
import com.liuheng.result.PageResult;
import com.liuheng.service.SetmealService;
import com.liuheng.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SetmealServiceImpl implements SetmealService {
    
    @Autowired
    private SetmealMapper setmealMapper;
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    
    @Override
    public boolean saveWithDish(SetmealDTO setmealDTO) {
        // TODO: implement Phase 2
        return false;
    }
    
    @Override
    public PageResult<SetmealVO> search(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询套餐：{}", setmealPageQueryDTO);
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        List<Setmeal> setmealList = setmealMapper.list(setmealPageQueryDTO);
        List<SetmealVO> setmealVOList = new ArrayList<>();
        
        for (Setmeal setmeal : setmealList) {
            SetmealVO setmealVO = new SetmealVO();
            BeanUtils.copyProperties(setmeal, setmealVO);
            
            // Get category name
            Category category = categoryMapper.getById(setmeal.getCategoryId());
            setmealVO.setCategoryName(category.getName());
            
            // Get associated dishes
            List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(setmeal.getId());
            setmealVO.setSetmealDishes(setmealDishes);
            
            setmealVOList.add(setmealVO);
        }
        
        return new PageResult<>(setmealVOList, PageInfo.of(setmealList).getTotal());
    }
    
    @Override
    public SetmealVO getById(Long id) {
        // TODO: implement Phase 1
        return null;
    }
    
    @Override
    public List<SetmealVO> getByCategoryId(Long categoryId) {
        // TODO: implement Phase 1
        return null;
    }
}
```

- [ ] **Step 2: Add list() method to SetmealMapper**

```java
@Select("SELECT * FROM setmeal WHERE name LIKE CONCAT('%', #{name}, '%') " +
        "AND category_id = #{categoryId} AND status = #{status}")
List<Setmeal> list(SetmealPageQueryDTO setmealPageQueryDTO);
```

- [ ] **Step 3: Add getById() method to CategoryMapper**

```java
@Select("SELECT * FROM category WHERE id = #{id}")
Category getById(Long id);
```

- [ ] **Step 4: Add getBySetmealId() method to SetmealDishMapper**

```java
@Select("SELECT * FROM setmeal_dish WHERE setmeal_id = #{setmealId}")
List<SetmealDish> getBySetmealId(Long setmealId);
```

- [ ] **Step 5: Test compilation**

Run: `mvn clean compile -pl sky-server`
Expected: SUCCESS with no compilation errors

- [ ] **Step 6: Commit**

```bash
git add sky-server/src/main/java/com/liuheng/service/impl/SetmealServiceImpl.java
git add sky-server/src/main/java/com/liuheng/mapper/SetmealMapper.java
git add sky-server/src/main/java/com/liuheng/mapper/CategoryMapper.java
git add sky-server/src/main/java/com/liuheng/mapper/SetmealDishMapper.java
git commit -m "feat: implement search method and required mapper methods"
```

### Task 4: Implement getById() and getByCategoryId() Methods

**Files:**
- Modify: `sky-server/src/main/java/com/liuheng/mapper/SetmealMapper.java` (add getById)
- Modify: `sky-server/src/main/java/com/liuheng/service/impl/SetmealServiceImpl.java` (implement methods)

- [ ] **Step 1: Add getById method to SetmealMapper**

```java
@Select("SELECT * FROM setmeal WHERE id = #{id}")
Setmeal getById(Long id);
```

- [ ] **Step 2: Implement getById() in SetmealServiceImpl**

```java
@Override
public SetmealVO getById(Long id) {
    log.info("根据ID查询套餐：{}", id);
    // Get setmeal basic info
    Setmeal setmeal = setmealMapper.getById(id);
    SetmealVO setmealVO = new SetmealVO();
    BeanUtils.copyProperties(setmeal, setmealVO);
    
    // Get category name
    Category category = categoryMapper.getById(setmeal.getCategoryId());
    setmealVO.setCategoryName(category.getName());
    
    // Get associated dishes
    List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
    setmealVO.setSetmealDishes(setmealDishes);
    
    return setmealVO;
}
```

- [ ] **Step 3: Implement getByCategoryId() in SetmealServiceImpl**

```java
@Override
public List<SetmealVO> getByCategoryId(Long categoryId) {
    log.info("根据分类ID查询套餐：{}", categoryId);
    // Only get active setmeals
    SetmealPageQueryDTO queryDTO = new SetmealPageQueryDTO();
    queryDTO.setCategoryId(categoryId.intValue());
    queryDTO.setStatus(1); // Only active
    
    List<Setmeal> setmealList = setmealMapper.list(queryDTO);
    List<SetmealVO> setmealVOList = new ArrayList<>();
    
    for (Setmeal setmeal : setmealList) {
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        
        // Get category name
        Category category = categoryMapper.getById(setmeal.getCategoryId());
        setmealVO.setCategoryName(category.getName());
        
        setmealVOList.add(setmealVO);
    }
    
    return setmealVOList;
}
```

- [ ] **Step 4: Test compilation**

Run: `mvn clean compile -pl sky-server`
Expected: SUCCESS with no compilation errors

- [ ] **Step 5: Commit**

```bash
git add sky-server/src/main/java/com/liuheng/mapper/SetmealMapper.java
git add sky-server/src/main/java/com/liuheng/service/impl/SetmealServiceImpl.java
git commit -m "feat: implement getById and getByCategoryId methods"
```

### Task 5: Update SetmealController with Query Endpoints

**Files:**
- Modify: `sky-server/src/main/java/com/liuheng/controller/admin/SetmealController.java`

- [ ] **Step 1: Add query endpoints to SetmealController**

```java
package com.liuheng.controller.admin;

import com.liuheng.dto.SetmealDTO;
import com.liuheng.dto.SetmealPageQueryDTO;
import com.liuheng.result.PageResult;
import com.liuheng.result.Result;
import com.liuheng.service.SetmealService;
import com.liuheng.vo.SetmealVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
@Tag(name = "套餐管理", description = "套餐相关接口")
@RequiredArgsConstructor
public class SetmealController {
    private final SetmealService setmealService;

    @Operation(summary = "新增套餐")
    @PostMapping
    public Result<Boolean> save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        Boolean bool = setmealService.saveWithDish(setmealDTO);
        return Result.success(bool);
    }

    @Operation(summary = "分页查询套餐")
    @PostMapping("/search")
    public Result<PageResult<SetmealVO>> search(@RequestBody SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询套餐：{}", setmealPageQueryDTO);
        PageResult<SetmealVO> pageResult = setmealService.search(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "根据ID查询套餐")
    @PostMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        log.info("根据ID查询套餐：{}", id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    @Operation(summary = "根据分类ID查询套餐")
    @PostMapping("/categoryid")
    public Result<List<SetmealVO>> getByCategoryId(@RequestParam Long categoryId) {
        log.info("根据分类ID查询套餐：{}", categoryId);
        List<SetmealVO> setmealVOList = setmealService.getByCategoryId(categoryId);
        return Result.success(setmealVOList);
    }
}
```

- [ ] **Step 2: Fix save method implementation**

Replace the existing save method with the correct method name:
```java
Boolean bool = setmealService.saveWithDish(setmealDTO);
```

- [ ] **Step 3: Test compilation**

Run: `mvn clean compile -pl sky-server`
Expected: SUCCESS with no compilation errors

- [ ] **Step 4: Commit**

```bash
git add sky-server/src/main/java/com/liuheng/controller/admin/SetmealController.java
git commit -m "feat: add query endpoints to SetmealController"
```

## Phase 2: Update and Delete Implementation

### Task 6: Create SetmealStatusDTO

**Files:**
- Create: `sky-pojo/src/main/java/com/liuheng/dto/SetmealStatusDTO.java`

- [ ] **Step 1: Create SetmealStatusDTO class**

```java
package com.liuheng.dto;

import lombok.Data;

@Data
public class SetmealStatusDTO {
    private Long id;
    private Integer status; // 0: 停用, 1: 启用
}
```

- [ ] **Step 2: Test compilation**

Run: `mvn clean compile -pl sky-pojo`
Expected: SUCCESS with no compilation errors

- [ ] **Step 3: Commit**

```bash
git add sky-pojo/src/main/java/com/liuheng/dto/SetmealStatusDTO.java
git commit -m "feat: add SetmealStatusDTO for status updates"
```

### Task 7: Enhance SetmealService Interface for Phase 2

**Files:**
- Modify: `sky-server/src/main/java/com/liuheng/service/SetmealService.java`

- [ ] **Step 1: Add update methods to interface**

```java
package com.liuheng.service;

import com.liuheng.dto.SetmealDTO;
import com.liuheng.dto.SetmealPageQueryDTO;
import com.liuheng.dto.SetmealStatusDTO;
import com.liuheng.vo.SetmealVO;
import com.liuheng.result.PageResult;

public interface SetmealService {
    boolean saveWithDish(SetmealDTO setmealDTO);
    
    // Phase 1 methods
    PageResult<SetmealVO> search(SetmealPageQueryDTO setmealPageQueryDTO);
    SetmealVO getById(Long id);
    List<SetmealVO> getByCategoryId(Long categoryId);
    
    // Phase 2 methods
    boolean update(SetmealDTO setmealDTO);
    boolean updateStatus(SetmealStatusDTO setmealStatusDTO);
    boolean delete(Long id);
}
```

- [ ] **Step 2: Test compilation**

Run: `mvn clean compile -pl sky-server`
Expected: SUCCESS with no compilation errors

- [ ] **Step 3: Commit**

```bash
git add sky-server/src/main/java/com/liuheng/service/SetmealService.java
git commit -m "feat: add update/delete methods to SetmealService interface"
```

### Task 8: Implement update() Method in Service Layer

**Files:**
- Modify: `sky-server/src/main/java/com/liuheng/service/impl/SetmealServiceImpl.java` (add implementation)
- Modify: `sky-server/src/main/java/com/liuheng/mapper/SetmealMapper.java` (add update method)

- [ ] **Step 1: Add update method to SetmealMapper**

```java
@Update("UPDATE setmeal SET name = #{name}, price = #{price}, status = #{status}, " +
        "description = #{description}, image = #{image}, update_time = #{updateTime}, " +
        "update_user = #{updateUser} WHERE id = #{id}")
int update(Setmeal setmeal);
```

- [ ] **Step 2: Implement update() in SetmealServiceImpl**

```java
@Override
@Transactional
public boolean update(SetmealDTO setmealDTO) {
    log.info("更新套餐：{}", setmealDTO);
    // Copy DTO to entity (preserve audit fields)
    Setmeal setmeal = new Setmeal();
    BeanUtils.copyProperties(setmealDTO, setmeal);
    setmeal.setUpdateTime(LocalDateTime.now());
    setmeal.setUpdateUser(BaseContext.getCurrentId());
    
    // Update basic info
    int updateResult = setmealMapper.update(setmeal);
    if (updateResult == 0) {
        return false;
    }
    
    // Update associated dishes - first delete old ones
    setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
    // Then insert new ones
    if (setmealDTO.getSetmealDishes() != null && !setmealDTO.getSetmealDishes().isEmpty()) {
        for (SetmealDish setmealDish : setmealDTO.getSetmealDishes()) {
            setmealDish.setSetmealId(setmealDTO.getId());
            setmealDishMapper.insert(setmealDish);
        }
    }
    
    return true;
}
```

- [ ] **Step 3: Add transaction support**

Add import: `import org.springframework.transaction.annotation.Transactional;`

- [ ] **Step 4: Add deleteBySetmealId method to SetmealDishMapper**

```java
@Delete("DELETE FROM setmeal_dish WHERE setmeal_id = #{setmealId}")
int deleteBySetmealId(Long setmealId);
```

- [ ] **Step 5: Test compilation**

Run: `mvn clean compile -pl sky-server`
Expected: SUCCESS with no compilation errors

- [ ] **Step 6: Commit**

```bash
git add sky-server/src/main/java/com/liuheng/service/impl/SetmealServiceImpl.java
git add sky-server/src/main/java/com/liuheng/mapper/SetmealMapper.java
git add sky-server/src/main/java/com/liuheng/mapper/SetmealDishMapper.java
git commit -m "feat: implement update method with transaction support"
```

### Task 9: Implement updateStatus() and delete() Methods

**Files:**
- Modify: `sky-server/src/main/java/com/liuheng/service/impl/SetmealServiceImpl.java` (add implementations)
- Modify: `sky-server/src/main/java/com/liuheng/mapper/SetmealMapper.java` (add methods)
- Create: `sky-server/src/main/java/com/liuheng/mapper/OrderMapper.java` (add method for delete validation)

- [ ] **Step 1: Add updateStatus method to SetmealMapper**

```java
@Update("UPDATE setmeal SET status = #{status}, update_time = #{updateTime}, " +
        "update_user = #{updateUser} WHERE id = #{id}")
int updateStatus(Setmeal setmeal);
```

- [ ] **Step 2: Add deleteById method to SetmealMapper**

```java
@Delete("DELETE FROM setmeal WHERE id = #{id}")
int deleteById(Long id);
```

- [ ] **Step 3: Create OrderMapper for delete validation**

```java
package com.liuheng.mapper;

import org.apache.ibatis.annotations.Select;

public interface OrderMapper {
    @Select("SELECT COUNT(*) FROM orders WHERE setmeal_id = #{setmealId}")
    int countBySetmealId(Long setmealId);
}
```

- [ ] **Step 4: Implement updateStatus() in SetmealServiceImpl**

```java
@Override
@Transactional
public boolean updateStatus(SetmealStatusDTO setmealStatusDTO) {
    log.info("套餐起售/停售：{}", setmealStatusDTO);
    // Validate status value
    if (setmealStatusDTO.getStatus() != 0 && setmealStatusDTO.getStatus() != 1) {
        throw new BaseException("状态值只能是0或1");
    }
    
    Setmeal setmeal = new Setmeal();
    setmeal.setId(setmealStatusDTO.getId());
    setmeal.setStatus(setmealStatusDTO.getStatus());
    setmeal.setUpdateTime(LocalDateTime.now());
    setmeal.setUpdateUser(BaseContext.getCurrentId());
    
    return setmealMapper.updateStatus(setmeal) > 0;
}
```

- [ ] **Step 5: Implement delete() in SetmealServiceImpl**

```java
@Override
@Transactional
public boolean delete(Long id) {
    log.info("删除套餐：{}", id);
    // Check if setmeal has orders
    int orderCount = orderMapper.countBySetmealId(id);
    if (orderCount > 0) {
        throw new BaseException("套餐关联了订单，不能删除");
    }
    
    // Delete associated dishes first
    setmealDishMapper.deleteBySetmealId(id);
    
    // Delete setmeal
    return setmealMapper.deleteById(id) > 0;
}
```

- [ ] **Step 6: Add OrderMapper dependency injection**

In SetmealServiceImpl:
```java
@Autowired
private OrderMapper orderMapper;
```

- [ ] **Step 7: Add required imports**

```java
import com.liuheng.exception.BaseException;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
```

- [ ] **Step 8: Test compilation**

Run: `mvn clean compile -pl sky-server`
Expected: SUCCESS with no compilation errors

- [ ] **Step 9: Commit**

```bash
git add sky-server/src/main/java/com/liuheng/service/impl/SetmealServiceImpl.java
git add sky-server/src/main/java/com/liuheng/mapper/SetmealMapper.java
git add sky-server/src/main/java/com/liuheng/mapper/OrderMapper.java
git commit -m "feat: implement updateStatus and delete methods with business rules"
```

### Task 10: Final SetmealController Implementation

**Files:**
- Modify: `sky-server/src/main/java/com/liuheng/controller/admin/SetmealController.java`

- [ ] **Step 1: Add update, status, and delete endpoints**

```java
@Operation(summary = "更新套餐")
@PostMapping("/update")
public Result<Boolean> update(@RequestBody @Valid SetmealDTO setmealDTO) {
    log.info("更新套餐：{}", setmealDTO);
    boolean bool = setmealService.update(setmealDTO);
    return Result.success(bool);
}

@Operation(summary = "套餐起售/停售")
@PostMapping("/status")
public Result<Boolean> updateStatus(@RequestBody @Valid SetmealStatusDTO setmealStatusDTO) {
    log.info("套餐起售/停售：{}", setmealStatusDTO);
    boolean bool = setmealService.updateStatus(setmealStatusDTO);
    return Result.success(bool);
}

@Operation(summary = "删除套餐")
@DeleteMapping("/delete")
public Result<Boolean> delete(@RequestParam Long id) {
    log.info("删除套餐：{}", id);
    boolean bool = setmealService.delete(id);
    return Result.success(bool);
}
```

- [ ] **Step 2: Add validation imports**

```java
import jakarta.validation.Valid;
```

- [ ] **Step 3: Test compilation**

Run: `mvn clean compile -pl sky-server`
Expected: SUCCESS with no compilation errors

- [ ] **Step 4: Commit**

```bash
git add sky-server/src/main/java/com/liuheng/controller/admin/SetmealController.java
git commit -m "feat: add update/delete endpoints to SetmealController"
```

## Testing and Validation

### Task 11: Integration Testing

- [ ] **Step 1: Start application**

Run: `mvn spring-boot:run -pl sky-server`
Expected: Application starts successfully on port 8090

- [ ] **Step 2: Test query endpoints with Postman/curl**

```bash
# Test search endpoint
curl -X POST http://localhost:8090/admin/setmeal/search \
  -H "Content-Type: application/json" \
  -d '{"page": 1, "pageSize": 10}'

# Test get by ID endpoint
curl -X POST http://localhost:8090/admin/setmeal/1

# Test get by category ID endpoint
curl -X POST "http://localhost:8090/admin/setmeal/categoryid?categoryId=1"
```

Expected: All endpoints return 200 OK with proper JSON response

- [ ] **Step 3: Test update/delete endpoints**

```bash
# Test update status
curl -X POST http://localhost:8090/admin/setmeal/status \
  -H "Content-Type: application/json" \
  -d '{"id": 1, "status": 0}'

# Test delete
curl -X DELETE http://localhost:8090/admin/setmeal/delete?id=1
```

Expected: Status updates and delete operations work correctly

- [ ] **Step 4: Test error cases**

```bash
# Test invalid status value
curl -X POST http://localhost:8090/admin/setmeal/status \
  -H "Content-Type: application/json" \
  -d '{"id": 1, "status": 2}'
```

Expected: Returns error with "状态值只能是0或1"

- [ ] **Step 5: Commit**

```bash
git add .
git commit -m "test: verify all SetmealController endpoints work correctly"
```

---

Plan complete and saved to `docs/superpowers/plans/2025-01-14-setmeal-controller-implementation.md`. Two execution options:

**1. Subagent-Driven (recommended)** - I dispatch a fresh subagent per task, review between tasks, fast iteration

**2. Inline Execution** - Execute tasks in this session using executing-plans, batch execution with checkpoints

Which approach?