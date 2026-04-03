package com.liuheng.mapper;

import com.liuheng.dto.DishFlavorDTO;
import com.liuheng.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    int save(List<DishFlavor> dishFlavorList);
}
