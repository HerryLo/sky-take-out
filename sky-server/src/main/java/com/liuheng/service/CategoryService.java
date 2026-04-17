package com.liuheng.service;

import com.liuheng.dto.CategoryDTO;
import com.liuheng.dto.CategoryPageQueryDTO;
import com.liuheng.dto.CategoryStatusDTO;
import com.liuheng.entity.Category;
import com.liuheng.result.PageResult;

import java.util.List;

public interface CategoryService {
    boolean save(CategoryDTO categoryDTO);
    PageResult search(CategoryPageQueryDTO categoryPageQueryDTO);
    boolean update(CategoryDTO categoryDTO);
    boolean updateStatus(CategoryStatusDTO categoryStatusDTO);
    boolean delete(Long id);
    List<Category> list(Integer type);
}
