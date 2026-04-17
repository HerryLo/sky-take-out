package com.liuheng.controller.admin;

import com.liuheng.constant.JwtClaimsConstant;
import com.liuheng.dto.*;
import com.liuheng.entity.Employee;
import com.liuheng.properties.JwtProperties;
import com.liuheng.result.PageResult;
import com.liuheng.result.Result;
import com.liuheng.service.EmployeeService;
import com.liuheng.utils.JwtUtil;
import com.liuheng.vo.EmployeeLoginVO;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController("adminEmployeeController")
@RequestMapping("/admin/employee")
@Tag(name = "员工管理", description = "员工相关接口")
public class EmployeeController {
    private EmployeeService EmployeeService;
    private JwtProperties jwtProperties;

    public EmployeeController(EmployeeService EmployeeService, JwtProperties jwtProperties) {
        this.EmployeeService = EmployeeService;
        this.jwtProperties = jwtProperties;
    }

    /**
     * 登录
     * @param empl
     * @return
     */
    @Operation(summary = "登录", security = @SecurityRequirement(name = ""))
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO empl) {
        log.info("登录: {}", empl);
        Employee employee = EmployeeService.login(empl);

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

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @Operation(summary = "新增员工")
    @PostMapping
    public Result<EmployeeDTO> save(@RequestBody @Valid EmployeeDTO employee) {
        log.info("新增员工: {}", employee);
        boolean bool = EmployeeService.save(employee);
        return bool ? Result.success(employee) : Result.success();
    }

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Operation(summary = "分页查询员工")
    @PostMapping("/search")
    public Result<PageResult> search(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("分页查询员工: {}", employeePageQueryDTO);
        PageResult pageResult = EmployeeService.search(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 更新员工信息
     * @param employeeDTO
     * @return
     */
    @Operation(summary = "编辑员工信息")
    @PostMapping("/update")
    public Result update(@RequestBody @Valid EmployeeDTO employeeDTO) {
        log.info("编辑员工信息: {}", employeeDTO);
        return Result.success(EmployeeService.update(employeeDTO));
    }

    /**
     * 启用禁用员工
     * @param employeeStatusDTO
     * @return
     */
    @Operation(summary = "启用禁用员工")
    @PostMapping("/status")
    public Result updateStatus(@RequestBody @Valid EmployeeStatusDTO employeeStatusDTO) {
        log.info("编辑员工信息: {}", employeeStatusDTO);
        return Result.success(EmployeeService.updateStatus(employeeStatusDTO));
    }
}
