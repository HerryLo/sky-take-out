package com.liuheng.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealMapper {
    Integer countByCategoryId(Long id);
}
