package com.liuheng.mapper;

import com.github.pagehelper.Page;
import com.liuheng.annotation.AutoFill;
import com.liuheng.dto.DishDTO;
import com.liuheng.dto.DishPageQueryDTO;
import com.liuheng.dto.DishStatusDTO;
import com.liuheng.entity.DishFlavor;
import com.liuheng.vo.DishVO;
import com.liuheng.entity.Dish;
import com.liuheng.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishMapper {
    /**
     * 分类id查询菜品
     * @param id
     * @return
     */
    Integer countByCategoryId(Long id);

    /**
     * 新增菜品
     * @param dish
     * @return
     */
    @AutoFill(value = OperationType.INSERT)
    int save(Dish dish);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 分页查询
     * @param name
     * @param categoryId
     * @param status
     * @return
     */
    List<Dish> list(String name, Long categoryId, Integer status);


    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    DishVO getById(Long id);

    /**
     * 根据分类id查询菜品
     * @param id
     * @return
     */
    List<Dish> getByCategoryId(Long id);

    /**
     * 更新菜品
     * @return
     */
    @AutoFill(value = OperationType.UPDATE)
    int update(Dish dish);

    /**
     * 菜品起售/停售
     * @param dish
     * @return
     */
    @AutoFill(value = OperationType.UPDATE)
    int updateStatus(Dish dish);
}
