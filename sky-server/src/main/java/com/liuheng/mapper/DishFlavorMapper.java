package com.liuheng.mapper;

import com.liuheng.annotation.AutoFill;
import com.liuheng.entity.DishFlavor;
import com.liuheng.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 新增菜品口味
     * @param dishFlavorList
     * @return
     */
    int save(List<DishFlavor> dishFlavorList);
}
