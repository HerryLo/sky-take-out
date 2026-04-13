package com.liuheng.controller.admin;

import com.liuheng.dto.SetmealDTO;
import com.liuheng.result.Result;
import com.liuheng.service.SetmealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
@Tag(name = "套餐管理", description = "套餐相关接口")
@RequiredArgsConstructor
public class SetmealController {
    private final SetmealService setmealService;

    @Operation(summary = "新增套餐")
    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        Boolean bool = setmealService.saveWithDish(setmealDTO);
        return Result.success(bool);
    }
}
