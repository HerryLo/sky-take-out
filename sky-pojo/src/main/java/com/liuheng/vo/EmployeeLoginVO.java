package com.liuheng.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 这是后台管理系统
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "员工登录返回VO")
public class EmployeeLoginVO implements Serializable {

    @Schema(description = "主键ID", example = "1")
    private Long id;

    @Schema(description = "用户名", example = "admin")
    private String userName;

    @Schema(description = "姓名", example = "张三")
    private String name;

    @Schema(description = "jwt令牌", example = "eyJhbGciOiJIUzI1NiIs....")
    private String token;

}
