package com.liuheng.controller.user;

import com.liuheng.entity.Category;
import com.liuheng.result.Result;
import com.liuheng.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@RequestMapping("/user/category")
@Tag(name = "分类", description = "分类相关接口")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    /**
     * 分类id查询菜品
     * @param type
     * @return
     */
    @PostMapping("/list")
    @Operation(summary = "查询分类")
    public Result list(Integer type) {
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
