package com.liuheng.controller.admin;

import com.liuheng.dto.CategoryDTO;
import com.liuheng.dto.CategoryPageQueryDTO;
import com.liuheng.dto.CategoryStatusDTO;
import com.liuheng.mapper.CategoryMapper;
import com.liuheng.result.PageResult;
import com.liuheng.result.Result;
import com.liuheng.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/category")
@Tag(name = "分类管理", description = "分类相关接口")
@SecurityRequirement(name = "tokenAuth")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;

    @Operation(summary = "新增分类")
    @PostMapping
    public Result<Boolean> save(@RequestBody CategoryDTO categoryDTO) {
        boolean bool = categoryService.save(categoryDTO);
        return Result.success(bool);
    }

    @Operation(summary = "分页查询分类")
    @PostMapping("/search")
    public Result<PageResult> search(@RequestBody CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pageResult = categoryService.search(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "更新分类")
    @PostMapping("/update")
    public Result<Boolean> update(@RequestBody CategoryDTO categoryDTO) {
        boolean bool = categoryService.update(categoryDTO);
        return Result.success(bool);
    }

    @Operation(summary = "开启禁用分类")
    @PostMapping("/status")
    public Result<Boolean> updateStatus(@RequestBody CategoryStatusDTO categoryStatusDTO) {
        boolean bool = categoryService.updateStatus(categoryStatusDTO);
        return Result.success(bool);
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/delete")
    public Result<Boolean> delete(Long id) {
        boolean bool = categoryService.delete(id);
        return Result.success(bool);
    }

}
