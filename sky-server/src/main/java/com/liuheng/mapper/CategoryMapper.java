package com.liuheng.mapper;

import com.liuheng.dto.CategoryPageQueryDTO;
import com.liuheng.dto.CategoryStatusDTO;
import com.liuheng.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    int save(Category category);
    List<Category> pageQuery(String name, Integer type);
    int update(Category category);
    int updateStatus(Long id , Integer status);
    Category getById(Long id);
    int deleteById(Long id);
    List<Category> list();
}
