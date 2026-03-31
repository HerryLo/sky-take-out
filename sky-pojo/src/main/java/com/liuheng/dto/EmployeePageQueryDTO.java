package com.liuheng.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Schema(description = "员工查询分页参数")
public class EmployeePageQueryDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 员工姓名（模糊查询）
     */
    @Schema(description = "员工姓名", example = "admin")
    private String name;

    /**
     * 员工id
     */
    @Schema(description = "员工id", example = "1")
    private Long id;

    /**
     * 账号状态：1-启用，0-禁用
     */
    @Schema(description = "账号状态", example = "1")
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
