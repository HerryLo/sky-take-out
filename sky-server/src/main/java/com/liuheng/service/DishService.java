package com.liuheng.service;

import com.liuheng.dto.DishDTO;
import com.liuheng.dto.DishPageQueryDTO;
import com.liuheng.entity.Dish;
import com.liuheng.result.PageResult;

import java.util.List;

public interface DishService {
    boolean saveWithFlavor(DishDTO dishDTO);
    PageResult search(DishPageQueryDTO dishPageQueryDTO);
}
