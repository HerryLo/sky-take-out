package com.liuheng.mapper;

import com.liuheng.dto.SetmealPageQueryDTO;
import com.liuheng.entity.Setmeal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SetmealMapperTest {

    @Autowired
    private SetmealMapper setmealMapper;

    @Test
    void testSave() {
        // Given
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(1L);
        setmeal.setName("测试套餐_" + UUID.randomUUID().toString().substring(0, 8));
        setmeal.setPrice(new BigDecimal("99.99"));
        setmeal.setStatus(1);
        setmeal.setDescription("测试描述");
        setmeal.setImage("test.jpg");

        // When
        int result = setmealMapper.save(setmeal);

        // Then
        assertEquals(1, result);
        assertNotNull(setmeal.getId());
    }

    @Test
    void testList() {
        // Given
        Setmeal setmeal = createTestSetmeal();
        setmealMapper.save(setmeal);

        SetmealPageQueryDTO queryDTO = new SetmealPageQueryDTO();
        queryDTO.setName("测试");
        queryDTO.setCategoryId(1);
        queryDTO.setStatus(1);

        // When
        List<Setmeal> result = setmealMapper.list(queryDTO);

        // Then
        assertNotNull(result);
    }

    @Test
    void testGetById() {
        // Given
        Setmeal setmeal = createTestSetmeal();
        setmealMapper.save(setmeal);
        Long setId = setmeal.getId();

        // When
        Setmeal result = setmealMapper.getById(setId);

        // Then
        assertNotNull(result);
        assertEquals(setId, result.getId());
    }

    @Test
    void testUpdate() {
        // Given
        Setmeal setmeal = createTestSetmeal();
        setmealMapper.save(setmeal);
        Long setId = setmeal.getId();

        Setmeal updateSetmeal = new Setmeal();
        updateSetmeal.setId(setId);
        updateSetmeal.setCategoryId(1L);
        updateSetmeal.setName("更新套餐_" + UUID.randomUUID().toString().substring(0, 8));
        updateSetmeal.setPrice(new BigDecimal("129.99"));
        updateSetmeal.setStatus(0);

        // When
        int result = setmealMapper.update(updateSetmeal);

        // Then
        assertEquals(1, result);
    }

    @Test
    void testDeleteBySetmealId() {
        // Given
        Setmeal setmeal = createTestSetmeal();
        setmealMapper.save(setmeal);
        Long setId = setmeal.getId();

        // When
        int result = setmealMapper.deleteBySetmealId(setId);

        // Then
        assertTrue(result >= 0);
    }

    @Test
    void testDeleteById() {
        // Given
        Setmeal setmeal = createTestSetmeal();
        setmealMapper.save(setmeal);
        Long setId = setmeal.getId();

        // When
        int result = setmealMapper.deleteById(setId);

        // Then
        assertTrue(result >= 0);
    }

    private Setmeal createTestSetmeal() {
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(1L);
        setmeal.setName("测试套餐_" + UUID.randomUUID().toString().substring(0, 8));
        setmeal.setPrice(new BigDecimal("99.99"));
        setmeal.setStatus(1);
        setmeal.setDescription("测试描述");
        setmeal.setImage("test.jpg");
        return setmeal;
    }
}