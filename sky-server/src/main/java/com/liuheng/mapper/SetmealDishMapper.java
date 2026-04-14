package com.liuheng.mapper;

import com.liuheng.annotation.AutoFill;
import com.liuheng.entity.Setmeal;
import com.liuheng.entity.SetmealDish;
import com.liuheng.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 批量新增套餐菜品
     * @param setmealDishes
     * @return
     */
    Integer save(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询套餐菜品
     * @param setmealId
     * @return
     */
    @Select("SELECT * FROM setmeal_dish WHERE setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);

    /**
     * 根据套餐id删除套餐菜品关系
     * @param setmealId
     * @return
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    Integer deleteBySetmealId(Long setmealId);
}
