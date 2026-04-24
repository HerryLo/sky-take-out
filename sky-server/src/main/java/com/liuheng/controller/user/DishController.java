package com.liuheng.controller.user;

import com.liuheng.constant.StatusConstant;
import com.liuheng.entity.Dish;
import com.liuheng.entity.DishFlavor;
import com.liuheng.result.Result;
import com.liuheng.service.DishService;
import com.liuheng.vo.DishVO;
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

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @PostMapping("/list")
    @Operation(summary = "分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品
        List<DishVO> dishFlavors = dishService.listWithFlavor(dish);
        return Result.success(dishFlavors);
    }
}
