package com.liuheng.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {
    @NotNull(message = "员工ID不能为空")
    @Schema(description = "id", example = "1")
    private Long id;

    @NotBlank(message = "用户名不能为空")           // 字符串非空
    @Size(min = 3, max = 20, message = "用户名长度3-20位")
    @Schema(description = "用户名（登录账号）", example = "admin")
    private String username;

    @NotBlank(message = "姓名不能为空")
    @Schema(description = "员工姓名", example = "1")
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误")
    @Schema(description = "手机号", example = "1")
    private String phone;

    @NotNull(message = "性别不能为空")                // Integer 用 @NotNull
    @Schema(description = "性别", example = "1")
    private String sex;

    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^\\d{17}[\\dXx]$", message = "身份证号格式错误")
    @Schema(description = "身份证", example = "1")
    private String idNumber;
}