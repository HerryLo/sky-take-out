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
