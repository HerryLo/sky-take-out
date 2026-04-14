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