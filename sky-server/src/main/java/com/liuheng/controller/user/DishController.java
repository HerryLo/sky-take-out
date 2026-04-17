package com.liuheng.controller.user;

import com.liuheng.entity.Dish;
import com.liuheng.entity.DishFlavor;
import com.liuheng.result.Result;
import com.liuheng.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Tag(name = "菜品", description = "菜品相关接口")
@RequiredArgsConstructor
public class DishController {
    private final DishService dishService;

    @PostMapping("/list")
    @Operation(summary = "分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId) {
        List<Dish> dishFlavors = dishService.list(categoryId);
        return Result.success(dishFlavors);
    }
}
