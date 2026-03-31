package com.liuheng.service;

import com.liuheng.dto.CategoryDTO;
import com.liuheng.dto.CategoryPageQueryDTO;
import com.liuheng.result.PageResult;

import java.util.List;

public interface CategoryService {
    boolean save(CategoryDTO categoryDTO);
    PageResult search(CategoryPageQueryDTO categoryPageQueryDTO);
}
