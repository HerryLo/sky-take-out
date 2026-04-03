package com.liuheng.mapper;

import com.liuheng.dto.DishDTO;
import com.liuheng.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper {
    Integer countByCategoryId(Long id);
    int save(Dish dish);
}
