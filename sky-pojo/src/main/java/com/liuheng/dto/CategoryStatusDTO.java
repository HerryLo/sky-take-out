package com.liuheng.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryStatusDTO {
    //主键
    @NotNull(message = "分类ID不能为空")
    @Schema(description = "id", example = "1")
    private Long id;

    //分类状态 0标识禁用 1表示启用
    @Schema(description = "分类状态：0标识禁用 1表示启用", example = "0")
    private Integer status;
}
