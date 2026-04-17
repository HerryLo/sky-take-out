package com.liuheng.controller.user;

import com.liuheng.result.Result;
import com.liuheng.service.SetmealService;
import com.liuheng.vo.DishItemVO;
import com.liuheng.vo.SetmealVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Tag(name = "套餐", description = "套餐相关接口")
@RequiredArgsConstructor
public class SetmealController {
    private final SetmealService setmealService;

    @PostMapping("/list")
    @Operation(summary = "分类id查询套餐")
    public Result list(Long categoryId) {
        List<SetmealVO> setmealVOS = setmealService.getByCategoryId(categoryId);
        return Result.success(setmealVOS);
    }

    @PostMapping("/dishlist")
    @Operation(summary = "套餐id查询包含的菜品")
    public Result dishList(Long id) {
        List<DishItemVO> dishes = setmealService.getDishById(id);
        return Result.success(dishes);
    }
}
