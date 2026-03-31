package com.liuheng.mapper;

import com.liuheng.dto.CategoryPageQueryDTO;
import com.liuheng.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    int save(Category category);
    List<Category> list(CategoryPageQueryDTO categoryPageQueryDTO);
}
