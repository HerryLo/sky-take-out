package com.liuheng.mapper;

import com.liuheng.dto.DishDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper {
    Integer countByCategoryId(Long id);
    int save(DishDTO dishDTO);
}
