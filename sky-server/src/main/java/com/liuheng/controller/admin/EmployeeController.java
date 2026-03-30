package com.liuheng.controller.admin;

import com.liuheng.constant.JwtClaimsConstant;
import com.liuheng.dto.EmployeeLoginDTO;
import com.liuheng.entity.Employee;
import com.liuheng.properties.JwtProperties;
import com.liuheng.result.Result;
import com.liuheng.service.EmployeeService;
import com.liuheng.utils.JwtUtil;
import com.liuheng.vo.EmployeeLoginVO;
import com.liuheng.vo.EmployeeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Tag(name = "员工管理", description = "员工相关接口")
public class EmployeeController {
    private EmployeeService employeeServiceImpl;
    private JwtProperties jwtProperties;

    public EmployeeController(EmployeeService employeeServiceImpl, JwtProperties jwtProperties) {
        this.employeeServiceImpl = employeeServiceImpl;
        this.jwtProperties = jwtProperties;
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO empl) {
        Employee employee = employeeServiceImpl.login(empl);

        HashMap<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());

        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
        return Result.success(employeeLoginVO);
    }

    @Operation(summary = "新增用户")
    @PostMapping
    public Result<Object> save(@RequestBody EmployeeVO employee) {
        employeeServiceImpl.save(employee);
        return Result.success();
    }
}
