package com.liuheng.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryDTO implements Serializable {
    //主键
    private Long id;

    //类型 1 菜品分类 2 套餐分类
    @NotBlank(message = "类型不能为空")
    @Schema(description = "类型", example = "1")
    private Integer type;

    //分类名称
    @NotBlank(message = "分类名称不能为空")
    @Schema(description = "分类名称", example = "1")
    private String name;

    //排序
    @Schema(description = "排序", example = "1")
    private Integer sort;
}
