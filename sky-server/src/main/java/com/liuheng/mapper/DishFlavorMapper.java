package com.liuheng.mapper;

import com.liuheng.dto.DishFlavorDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishFlavorMapper {
    int save(DishFlavorDTO dishFlavorDTO);
}
