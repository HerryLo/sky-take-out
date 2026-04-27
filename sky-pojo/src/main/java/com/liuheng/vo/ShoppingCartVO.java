package com.liuheng.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartVO {
    private Long id;              // 购物车项ID
    private String name;          // 商品名称
    private String image;         // 图片
    private String dishFlavor;    // 口味
    private Integer number;       // 数量
    private BigDecimal amount;    // 金额
    private Long categoryId;      // 分类ID
    private String categoryName;  // 分类名称
    private String type;          // 商品类型：dish/setmeal
}