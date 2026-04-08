package com.liuheng.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DishStatusDTO {
    /**
     * 0 停售 1 起售
     */
    private Integer status;

    @NotNull(message = "菜品ID不能为空")
    @Schema(description = "id", example = "1")
    private Long id;
}
