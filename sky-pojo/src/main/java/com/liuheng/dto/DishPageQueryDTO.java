package com.liuheng.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DishPageQueryDTO {
    private String name;

    private Long categoryId;

    private Integer status;

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
}
