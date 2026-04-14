package com.liuheng.controller.admin;

import com.liuheng.dto.SetmealDTO;
import com.liuheng.dto.SetmealPageQueryDTO;
import com.liuheng.result.PageResult;
import com.liuheng.result.Result;
import com.liuheng.service.SetmealService;
import com.liuheng.vo.SetmealVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Operation(summary = "分页查询套餐")
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.search(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "根据ID查询套餐")
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    @Operation(summary = "根据分类ID查询套餐")
    @GetMapping("/category/{categoryId}")
    public Result<List<SetmealVO>> getByCategoryId(@PathVariable Long categoryId) {
        List<SetmealVO> setmealVOList = setmealService.getByCategoryId(categoryId);
        return Result.success(setmealVOList);
    }

    @Operation(summary = "修改套餐")
    @PutMapping
    public Result<Boolean> update(@RequestBody SetmealDTO setmealDTO) {
        Boolean bool = setmealService.updateWithDish(setmealDTO);
        return Result.success(bool);
    }

    @Operation(summary = "删除套餐")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        Boolean bool = setmealService.delete(id);
        return Result.success(bool);
    }

    @Operation(summary = "启用/禁用套餐")
    @PostMapping("/status/{status}")
    public Result<Boolean> changeStatus(@PathVariable Integer status, @RequestParam Long id) {
        Boolean bool = setmealService.changeStatus(status, id);
        return Result.success(bool);
    }
}
