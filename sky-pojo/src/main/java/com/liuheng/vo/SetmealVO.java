package com.liuheng.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 套餐数据传输对象
 */
@Data
public class SetmealVO {

    private Long id;

    private String name;

    private BigDecimal price;

    private String image;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer status;

    private Long categoryId;

    private String categoryName;

    private List<DishVO> dishes;
}