package com.liuheng.controller.admin;

import com.liuheng.dto.DishDTO;
import com.liuheng.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Tag(name = "菜品管理", description = "菜品相关接口")
@SecurityRequirement(name = "tokenAuth")
public class DishController {

    @Operation(summary = "新增菜品")
    @PostMapping
    public Result saveWithFlavor(@RequestBody DishDTO dishDTO){
        return Result.success();
    }
}
