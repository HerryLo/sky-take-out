package com.liuheng.controller.admin;

import com.liuheng.dto.CategoryDTO;
import com.liuheng.dto.CategoryPageQueryDTO;
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
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        boolean bool = categoryService.save(categoryDTO);
        return Result.success(bool);
    }

    @Operation(summary = "分页查询分类")
    @PostMapping("/search")
    public Result search(@RequestBody CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pageResult = categoryService.search(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

}
