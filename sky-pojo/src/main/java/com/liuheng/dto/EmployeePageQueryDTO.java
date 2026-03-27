package com.liuheng.dto;

import java.io.Serial;
import java.io.Serializable;

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
