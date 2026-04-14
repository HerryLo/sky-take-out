package com.liuheng.mapper;

import com.liuheng.annotation.AutoFill;
import com.liuheng.dto.SetmealPageQueryDTO;
import com.liuheng.entity.Setmeal;
import com.liuheng.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {
    /**
     * 分类id查询套餐
     * @param id
     * @return
     */
    Integer countByCategoryId(Long id);

    /**
     * 新增套餐
     * @param setmeal
     * @return
     */
    @AutoFill(value = OperationType.INSERT)
    Integer save(Setmeal setmeal);

    /**
     * 条件查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    @Select("SELECT * FROM setmeal WHERE name LIKE CONCAT('%', #{name}, '%') " +
            "AND category_id = #{categoryId} AND status = #{status}")
    List<Setmeal> list(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据ID查询套餐
     * @param id
     * @return
     */
    @Select("SELECT * FROM setmeal WHERE id = #{id}")
    Setmeal getById(Long id);

    /**
     * 修改套餐
     * @param setmeal
     * @return
     */
    @AutoFill(value = OperationType.UPDATE)
    Integer update(Setmeal setmeal);

    /**
     * 根据套餐ID删除关联的菜品
     * @param setmealId
     * @return
     */
    Integer deleteBySetmealId(Long setmealId);

    /**
     * 根据ID删除套餐
     * @param id
     * @return
     */
    Integer deleteById(Long id);
}
