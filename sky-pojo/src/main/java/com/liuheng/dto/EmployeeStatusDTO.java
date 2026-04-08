package com.liuheng.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeStatusDTO  implements Serializable {
    /**
     * 账号状态：1-启用，0-禁用
     */
    private Integer status;

    @NotNull(message = "员工ID不能为空")
    @Schema(description = "id", example = "1")
    private Long id;
}
