package com.liuheng.controller.admin;

import com.liuheng.dto.DishDTO;
import com.liuheng.dto.DishPageQueryDTO;
import com.liuheng.dto.DishStatusDTO;
import com.liuheng.entity.Dish;
import com.liuheng.entity.DishFlavor;
import com.liuheng.result.PageResult;
import com.liuheng.result.Result;
import com.liuheng.service.DishService;
import com.liuheng.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Tag(name = "菜品管理", description = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @Operation(summary = "新增菜品")
    @PostMapping
    public Result saveWithFlavor(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}",dishDTO);
        boolean bool = dishService.saveWithFlavor(dishDTO);
        return Result.success(bool);
    }

    @Operation(summary = "分页查询菜品")
    @PostMapping("/search")
    public Result search(@RequestBody DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询菜品：{}",dishPageQueryDTO);
        PageResult pageResult = dishService.search(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "查询菜品口味")
    @PostMapping("/flavor")
    public Result getFlavorByDishId(Long id) {
        log.info("查询根据菜品id查询口味：{}",id);
        List<DishFlavor> dishFlavorList = dishService.getFlavorByDishId(id);
        return Result.success(dishFlavorList);
    }

    @Operation(summary = "根据菜品id查询菜品")
    @PostMapping("/dishid")
    public Result<DishVO> getById(Long id){
        log.info("根据菜品id查询菜品：{}",id);
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    @Operation(summary = "根据分类id查询菜品")
    @PostMapping("/categoryid")
    public Result<List<Dish>> getByCategoryId(Long categoryId) {
        log.info("根据分类id查询菜品：{}",categoryId);
        List<Dish> dishes = dishService.getByCategoryId(categoryId);
        return Result.success(dishes);
    }

    @Operation(summary = "更新菜品")
    @PostMapping("/update")
    public Result update(@RequestBody @Validated DishDTO dishDTO){
        log.info("更新菜品：{}",dishDTO);
        Boolean bool = dishService.update(dishDTO);
        return  Result.success(bool);
    }

    @Operation(summary = "菜品起售/停售")
    @PostMapping("/status")
    public Result updateStatus(@RequestBody @Valid DishStatusDTO dishStatusDTO){
        log.info("菜品起售/停售：{}",dishStatusDTO);
        Boolean bool = dishService.updateStatus(dishStatusDTO);
        return Result.success(bool);
    }
}
