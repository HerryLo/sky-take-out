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