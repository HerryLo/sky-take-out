package com.liuheng.controller.admin;

import com.liuheng.dto.SetmealDTO;
import com.liuheng.dto.SetmealPageQueryDTO;
import com.liuheng.result.PageResult;
import com.liuheng.result.Result;
import com.liuheng.service.SetmealService;
import com.liuheng.vo.SetmealVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
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

    @Test
    void testSave() {
        // Given
        SetmealDTO setmealDTO = new SetmealDTO();
        setmealDTO.setName("测试套餐");
        setmealDTO.setCategoryId(1L);
        setmealDTO.setPrice(new BigDecimal("99.99"));
        setmealDTO.setStatus(1);

        List<com.liuheng.entity.SetmealDish> dishes = new ArrayList<>();
        com.liuheng.entity.SetmealDish dish = new com.liuheng.entity.SetmealDish();
        dish.setDishId(1L);
        dishes.add(dish);
        setmealDTO.setSetmealDishes(dishes);

        when(setmealService.saveWithDish(any(SetmealDTO.class))).thenReturn(true);

        // When
        Result<Boolean> result = setmealController.save(setmealDTO);

        // Then
        assertNotNull(result);
        assertTrue(result.getData());
        verify(setmealService, times(1)).saveWithDish(any(SetmealDTO.class));
    }

    @Test
    void testPage() {
        // Given
        SetmealPageQueryDTO queryDTO = new SetmealPageQueryDTO();
        queryDTO.setPage(1);
        queryDTO.setPageSize(10);

        PageResult pageResult = new PageResult();
        pageResult.setTotal(1);
        pageResult.setRecords(new ArrayList<>());

        when(setmealService.search(queryDTO)).thenReturn(pageResult);

        // When
        Result<PageResult> result = setmealController.page(queryDTO);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(1, result.getData().getTotal());
    }

    @Test
    void testGetById() {
        // Given
        Long setId = 1L;
        SetmealVO setmealVO = new SetmealVO();
        setmealVO.setId(setId);
        setmealVO.setName("测试套餐");

        when(setmealService.getById(setId)).thenReturn(setmealVO);

        // When
        Result<SetmealVO> result = setmealController.getById(setId);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(setId, result.getData().getId());
    }

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
        Result<List<SetmealVO>> result = setmealController.getByCategoryId(categoryId);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
    }

    @Test
    void testUpdate() {
        // Given
        SetmealDTO setmealDTO = new SetmealDTO();
        setmealDTO.setId(1L);
        setmealDTO.setName("更新套餐");

        when(setmealService.updateWithDish(any(SetmealDTO.class))).thenReturn(true);

        // When
        Result<Boolean> result = setmealController.update(setmealDTO);

        // Then
        assertNotNull(result);
        assertTrue(result.getData());
        verify(setmealService, times(1)).updateWithDish(any(SetmealDTO.class));
    }

    @Test
    void testDelete() {
        // Given
        Long setId = 1L;
        when(setmealService.delete(setId)).thenReturn(true);

        // When
        Result<Boolean> result = setmealController.delete(setId);

        // Then
        assertNotNull(result);
        assertTrue(result.getData());
        verify(setmealService, times(1)).delete(setId);
    }

    @Test
    void testChangeStatus() {
        // Given
        Long setId = 1L;
        Integer status = 0;
        when(setmealService.changeStatus(status, setId)).thenReturn(true);

        // When
        Result<Boolean> result = setmealController.changeStatus(status, setId);

        // Then
        assertNotNull(result);
        assertTrue(result.getData());
        verify(setmealService, times(1)).changeStatus(status, setId);
    }
}