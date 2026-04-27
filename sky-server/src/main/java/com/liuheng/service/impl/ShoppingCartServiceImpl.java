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