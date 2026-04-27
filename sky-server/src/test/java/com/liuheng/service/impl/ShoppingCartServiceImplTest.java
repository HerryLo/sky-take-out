package com.liuheng.service.impl;

import com.liuheng.context.BaseContext;
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
import static org.mockito.Mockito.lenient;

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
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testList_WhenCacheEmpty_ShouldQueryDatabase() {
        // Given
        BaseContext.setCurrentId(1L);
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
        // Given
        BaseContext.setCurrentId(1L);

        // When
        shoppingCartService.clean();

        // Then
        verify(shoppingCartMapper).deleteByUserId(1L);
        verify(redisTemplate).delete(anyString());
    }
}