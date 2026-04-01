package com.liuheng.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper {
    Integer countByCategoryId(Long id);
}
