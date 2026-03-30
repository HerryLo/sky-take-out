package com.liuheng.controller.admin;

import com.liuheng.constant.JwtClaimsConstant;
import com.liuheng.dto.EmployeeLoginDTO;
import com.liuheng.dto.EmployeePageQueryDTO;
import com.liuheng.entity.Employee;
import com.liuheng.properties.JwtProperties;
import com.liuheng.result.Result;
import com.liuheng.service.EmployeeService;
import com.liuheng.utils.JwtUtil;
import com.liuheng.vo.EmployeeLoginVO;
import com.liuheng.vo.EmployeeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/admin/employee")
@Tag(name = "员工管理", description = "员工相关接口")
@SecurityRequirement(name = "tokenAuth")
public class EmployeeController {
    private EmployeeService employeeServiceImpl;
    private JwtProperties jwtProperties;

    public EmployeeController(EmployeeService employeeServiceImpl, JwtProperties jwtProperties) {
        this.employeeServiceImpl = employeeServiceImpl;
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

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @Operation(summary = "新增员工")
    @PostMapping
    public Result<EmployeeVO> save(@RequestBody EmployeeVO employee) {
        boolean bool = employeeServiceImpl.save(employee);
        return bool ? Result.success(employee) : Result.success();
    }

    /**
     * 分页查询员工
     * @return
     */
    @Operation(summary = "员工分页查询")
    @PostMapping("/search")
    public Result<Object> search(EmployeePageQueryDTO employeePageQueryDTO) {
        return Result.success();
    }
}
