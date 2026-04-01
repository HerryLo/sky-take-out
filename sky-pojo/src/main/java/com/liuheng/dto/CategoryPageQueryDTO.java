package com.liuheng.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryPageQueryDTO implements Serializable {
    /**
     * 页码
     */
    @Schema(description = "页码", example = "1")
    private int page = 1;

    /**
     * 每页记录数
     */
    @Schema(description = "每页记录数", example = "10")
    private int pageSize = 10;

    //分类名称
    @Schema(description = "分类名称", example = "鸡蛋")
    private String name;

    //分类类型 1菜品分类  2套餐分类
    @Schema(description = "分类类型", example = "1")
    private Integer type;
}
