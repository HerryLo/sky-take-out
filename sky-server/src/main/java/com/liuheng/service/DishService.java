package com.liuheng.service;

import com.liuheng.dto.DishDTO;
import com.liuheng.dto.DishPageQueryDTO;
import com.liuheng.dto.DishStatusDTO;
import com.liuheng.entity.Dish;
import com.liuheng.entity.DishFlavor;
import com.liuheng.result.PageResult;
import com.liuheng.vo.DishVO;

import java.util.List;

public interface DishService {
    boolean saveWithFlavor(DishDTO dishDTO);
    PageResult search(DishPageQueryDTO dishPageQueryDTO);
    List<DishFlavor> getFlavorByDishId(Long id);
    DishVO getById(Long id);
    List<Dish> getByCategoryId(Long categoryId);
    boolean update(DishDTO dishDTO);
    boolean updateStatus(DishStatusDTO dishStatusDTO);
    List<Dish> list(Long categoryId);
}
