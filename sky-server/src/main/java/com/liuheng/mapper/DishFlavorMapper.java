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

    /**
     * 菜品id查询菜品口味
     * @param id
     * @return
     */
    List<DishFlavor> getFlavorByDishId(Long id);

    /**
     * 根据菜品id删除菜品口味
     * @param dishId
     */
    void deleteByDishId(Long dishId);
}
