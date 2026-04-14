# SetmealController Phased Implementation Design

## Overview
Design document for implementing the remaining functionality of SetmealController in Spring Boot food delivery backend system. Implementation follows a phased approach: Query methods first, then Update/Delete operations.

## Phase 1: Query Methods Implementation

### 1.1 Create SetmealVO Class
**File:** `sky-pojo/src/main/java/com/liuheng/vo/SetmealVO.java`
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

### 1.2 Enhance SetmealService Interface
**File:** `sky-server/src/main/java/com/liuheng/service/SetmealService.java`
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

### 1.3 Implement Service Layer Methods
**File:** `sky-server/src/main/java/com/liuheng/service/impl/SetmealServiceImpl.java` (create new)

#### search() Method
```java
@Override
public PageResult<SetmealVO> search(SetmealPageQueryDTO setmealPageQueryDTO) {
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
```

#### getById() Method
```java
@Override
public SetmealVO getById(Long id) {
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

#### getByCategoryId() Method
```java
@Override
public List<SetmealVO> getByCategoryId(Long categoryId) {
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

### 1.4 Add Mapper Methods
**File:** `sky-server/src/main/java/com/liuheng/mapper/SetmealMapper.java` (add methods)

```java
@Select("SELECT * FROM setmeal WHERE name LIKE CONCAT('%', #{name}, '%') AND category_id = #{categoryId} AND status = #{status}")
List<Setmeal> list(SetmealPageQueryDTO setmealPageQueryDTO);

@Select("SELECT * FROM setmeal WHERE id = #{id}")
Setmeal getById(Long id);
```

**File:** `sky-server/src/main/java/com/liuheng/mapper/SetmealDishMapper.java` (add method)

```java
@Select("SELECT * FROM setmeal_dish WHERE setmeal_id = #{setmealId}")
List<SetmealDish> getBySetmealId(Long setmealId);
```

### 1.5 Update SetmealController for Phase 1
**File:** `sky-server/src/main/java/com/liuheng/controller/admin/SetmealController.java`

```java
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
```

## Phase 2: Update and Delete Implementation

### 2.1 Create SetmealStatusDTO
**File:** `sky-pojo/src/main/java/com/liuheng/dto/SetmealStatusDTO.java`
```java
package com.liuheng.dto;

import lombok.Data;

@Data
public class SetmealStatusDTO {
    private Long id;
    private Integer status; // 0: 停用, 1: 启用
}
```

### 2.2 Enhance SetmealService Interface for Phase 2
**File:** `sky-server/src/main/java/com/liuheng/service/SetmealService.java` (add methods)

```java
// Phase 2 methods
boolean update(SetmealDTO setmealDTO);
boolean updateStatus(SetmealStatusDTO setmealStatusDTO);
boolean delete(Long id);
```

### 2.3 Implement Update Service Methods
**File:** `sky-server/src/main/java/com/liuheng/service/impl/SetmealServiceImpl.java` (add methods)

#### update() Method
```java
@Override
@Transactional
public boolean update(SetmealDTO setmealDTO) {
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

#### updateStatus() Method
```java
@Override
@Transactional
public boolean updateStatus(SetmealStatusDTO setmealStatusDTO) {
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

#### delete() Method
```java
@Override
@Transactional
public boolean delete(Long id) {
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

### 2.4 Add Additional Mapper Methods
**File:** `sky-server/src/main/java/com/liuheng/mapper/SetmealMapper.java` (add methods)

```java
@Update("UPDATE setmeal SET name = #{name}, price = #{price}, status = #{status}, " +
        "description = #{description}, image = #{image}, update_time = #{updateTime}, " +
        "update_user = #{updateUser} WHERE id = #{id}")
int update(Setmeal setmeal);

@Update("UPDATE setmeal SET status = #{status}, update_time = #{updateTime}, " +
        "update_user = #{updateUser} WHERE id = #{id}")
int updateStatus(Setmeal setmeal);

@Delete("DELETE FROM setmeal WHERE id = #{id}")
int deleteById(Long id);
```

### 2.5 Final SetmealController Implementation
**File:** `sky-server/src/main/java/com/liuheng/controller/admin/SetmealController.java`

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

## Testing Strategy

### Unit Tests
Create `sky-server/src/test/java/com/liuheng/service/impl/SetmealServiceImplTest.java`

```java
@SpringBootTest
class SetmealServiceImplTest {
    
    @Autowired
    private SetmealService setmealService;
    
    @Test
    void testSearch() {
        // Test search with pagination
    }
    
    @Test
    void testGetById() {
        // Test get by ID returns complete VO
    }
    
    @Test
    void testUpdate() {
        // Test update preserves audit fields
    }
    
    @Test
    void testUpdateStatus() {
        // Test status toggle functionality
    }
    
    @Test
    void testDelete() {
        // Test delete with business rules
    }
}
```

### Integration Tests
Test endpoints with Postman or similar tool:
- Verify all endpoints return correct HTTP status codes
- Test JSON response structure matches expectations
- Test error handling for invalid inputs
- Test pagination functionality

## Risk Mitigation

### Data Consistency
- Use `@Transactional` for update/delete operations
- Implement proper validation for all inputs
- Handle concurrent updates with optimistic locking

### Error Handling
- Custom exceptions for business rule violations
- Consistent error response format
- Proper logging for debugging

### Performance
- Use PageHelper for pagination
- Implement lazy loading for large relationships
- Consider caching for frequently accessed data

## Success Criteria

### Phase 1 Complete When:
- [ ] All query endpoints return 200 OK
- [ ] Pagination works correctly with PageHelper
- [ ] SetmealVO includes category name and dishes
- [ ] All unit tests pass for query methods

### Phase 2 Complete When:
- [ ] Update operations preserve audit fields
- [ ] Status toggle works bidirectional
- [ ] Delete respects business rules (no orders)
- [ ] All integration tests pass
- [ ] End-to-end workflow tested

## Dependencies
- MyBatis mappers for Setmeal and SetmealDish
- Category entity and mapper for category name lookup
- Order mapper for delete validation
- BaseContext for user audit information
- Global exception handling for consistent error responses