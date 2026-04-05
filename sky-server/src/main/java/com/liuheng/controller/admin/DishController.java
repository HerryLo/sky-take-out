package com.liuheng.controller.admin;

import com.liuheng.dto.DishDTO;
import com.liuheng.dto.DishPageQueryDTO;
import com.liuheng.result.PageResult;
import com.liuheng.result.Result;
import com.liuheng.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private DishService dishService;

    @Operation(summary = "新增菜品")
    @PostMapping
    public Result saveWithFlavor(@RequestBody DishDTO dishDTO){
        boolean bool = dishService.saveWithFlavor(dishDTO);
        return Result.success(bool);
    }

    @Operation(summary = "分页查询菜品")
    @PostMapping("/search")
    public Result search(@RequestBody DishPageQueryDTO dishPageQueryDTO){
        PageResult pageResult = dishService.search(dishPageQueryDTO);
        return Result.success(pageResult);
    }
}
