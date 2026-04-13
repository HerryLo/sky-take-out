package com.liuheng.mapper;

import com.liuheng.annotation.AutoFill;
import com.liuheng.entity.Setmeal;
import com.liuheng.entity.SetmealDish;
import com.liuheng.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 批量新增套餐菜品
     * @param setmealDishes
     * @return
     */
    @AutoFill(value = OperationType.INSERT)
    Integer save(List<SetmealDish> setmealDishes);
}
