package com.liuheng.service;

import com.liuheng.dto.SetmealDTO;
import com.liuheng.dto.SetmealPageQueryDTO;
import com.liuheng.entity.SetmealDish;
import com.liuheng.vo.DishItemVO;
import com.liuheng.vo.SetmealVO;
import com.liuheng.result.PageResult;
import java.util.List;

public interface SetmealService {
    boolean saveWithDish(SetmealDTO setmealDTO);

    // Phase 1 methods
    PageResult search(SetmealPageQueryDTO setmealPageQueryDTO);
    SetmealVO getById(Long id);
    List<SetmealVO> getByCategoryId(Long categoryId);

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    boolean updateWithDish(SetmealDTO setmealDTO);

    /**
     * 删除套餐
     * @param id
     * @return
     */
    boolean delete(Long id);

    /**
     * 启用/禁用套餐
     * @param status
     * @param id
     * @return
     */
    boolean changeStatus(Integer status, Long id);


    List<DishItemVO> getDishById(Long id);
}
