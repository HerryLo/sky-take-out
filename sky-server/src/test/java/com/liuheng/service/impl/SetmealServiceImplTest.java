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
import com.liuheng.vo.SetmealVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Clear PageHelper thread-local state to avoid test pollution
        com.github.pagehelper.PageHelper.clearPage();
        closeable.close();
    }

    @Test
    void testSaveWithDish() {
        // Given
        SetmealDTO setmealDTO = new SetmealDTO();
        setmealDTO.setName("测试套餐");
        setmealDTO.setCategoryId(1L);
        setmealDTO.setPrice(BigDecimal.valueOf(99.99));
        setmealDTO.setStatus(1);

        List<SetmealDish> dishes = new ArrayList<>();
        SetmealDish dish = new SetmealDish();
        dish.setDishId(1L);
        dish.setName("测试菜品");
        dish.setPrice(BigDecimal.valueOf(19.99));
        dishes.add(dish);
        setmealDTO.setSetmealDishes(dishes);

        when(setmealMapper.save(any(Setmeal.class))).thenReturn(1);
        when(setmealDishMapper.save(anyList())).thenReturn(1);

        // When
        boolean result = setmealService.saveWithDish(setmealDTO);

        // Then
        assertTrue(result);
        verify(setmealMapper, times(1)).save(any(Setmeal.class));
        verify(setmealDishMapper, times(1)).save(anyList());
    }

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
        Setmeal setmeal = Setmeal.builder()
                .id(1L)
                .name("测试套餐")
                .categoryId(1L)
                .status(1)
                .build();
        setmealList.add(setmeal);

        when(setmealMapper.list(any(SetmealPageQueryDTO.class))).thenReturn(setmealList);
        when(categoryMapper.getById(1L)).thenReturn(
                Category.builder().id(1L).name("测试分类").build()
        );
        when(setmealDishMapper.getBySetmealId(1L)).thenReturn(new ArrayList<>());

        // When
        PageResult result = setmealService.search(queryDTO);

        // Then
        assertNotNull(result);
    }

    @Test
    void testGetById() {
        // Given
        Long setId = 1L;
        Setmeal setmeal = Setmeal.builder()
                .id(setId)
                .name("测试套餐")
                .categoryId(1L)
                .status(1)
                .build();

        when(setmealMapper.getById(setId)).thenReturn(setmeal);
        when(categoryMapper.getById(1L)).thenReturn(
                Category.builder().id(1L).name("测试分类").build()
        );
        when(setmealDishMapper.getBySetmealId(setId)).thenReturn(new ArrayList<>());

        // When
        SetmealVO result = setmealService.getById(setId);

        // Then
        assertNotNull(result);
        assertEquals(setId, result.getId());
        assertEquals("测试套餐", result.getName());
    }

    @Test
    void testGetByCategoryId() {
        // Given
        Long categoryId = 1L;

        List<Setmeal> setmealList = new ArrayList<>();
        Setmeal setmeal = Setmeal.builder()
                .id(1L)
                .name("测试套餐")
                .categoryId(categoryId)
                .status(1)
                .build();
        setmealList.add(setmeal);

        when(setmealMapper.list(any(SetmealPageQueryDTO.class))).thenReturn(setmealList);
        when(categoryMapper.getById(categoryId)).thenReturn(
                Category.builder().id(categoryId).name("测试分类").build()
        );

        // When
        List<SetmealVO> result = setmealService.getByCategoryId(categoryId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categoryId, result.get(0).getCategoryId());
    }

    @Test
    void testUpdateWithDish() {
        // Given
        SetmealDTO setmealDTO = new SetmealDTO();
        setmealDTO.setId(1L);
        setmealDTO.setName("更新套餐");
        setmealDTO.setCategoryId(1L);
        setmealDTO.setPrice(BigDecimal.valueOf(129.99));
        setmealDTO.setStatus(1);

        List<SetmealDish> dishes = new ArrayList<>();
        SetmealDish dish = new SetmealDish();
        dish.setDishId(1L);
        dish.setName("更新菜品");
        dishes.add(dish);
        setmealDTO.setSetmealDishes(dishes);

        when(setmealMapper.getById(1L)).thenReturn(
                Setmeal.builder().id(1L).name("旧套餐").categoryId(1L)
                        .price(BigDecimal.valueOf(99.99)).status(1).build()
        );
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

    @Test
    void testDelete() {
        // Given
        Long setId = 1L;
        when(setmealMapper.getById(setId)).thenReturn(
                Setmeal.builder().id(setId).name("测试套餐").categoryId(1L)
                        .price(BigDecimal.valueOf(99.99)).status(1).build()
        );
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

    @Test
    void testChangeStatus() {
        // Given
        Long setId = 1L;
        Integer status = 0;
        when(setmealMapper.getById(setId)).thenReturn(
                Setmeal.builder().id(setId).name("测试套餐").categoryId(1L)
                        .price(BigDecimal.valueOf(99.99)).status(1).build()
        );
        when(setmealMapper.update(any(Setmeal.class))).thenReturn(1);

        // When
        boolean result = setmealService.changeStatus(status, setId);

        // Then
        assertTrue(result);
        verify(setmealMapper, times(1)).getById(setId);
        verify(setmealMapper, times(1)).update(any(Setmeal.class));
    }
}
