package com.liuheng.dto;

import com.liuheng.entity.DishFlavor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class DishDTO {
    // 菜品基本信息
    private Long id;
    private String name;
    private BigDecimal price;
    private Long categoryId;
    private String image;
    private String description;
    private Integer status;

    // 口味列表
    private List<DishFlavor> flavors;
}
