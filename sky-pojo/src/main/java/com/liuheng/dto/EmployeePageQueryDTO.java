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
    private String name;

    /**
     * 页码
     */
    private int page = 1;

    /**
     * 每页记录数
     */
    private int pageSize = 10;
}
