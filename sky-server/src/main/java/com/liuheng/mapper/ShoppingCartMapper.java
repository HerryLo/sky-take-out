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