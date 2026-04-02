package com.liuheng.service.impl;

import com.liuheng.dto.DishDTO;
import com.liuheng.mapper.DishMapper;
import com.liuheng.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @Override
    public boolean saveWithFlavor(DishDTO dishDTO) {
        dishMapper.save(dishDTO);

         // TODO 新增菜品同时还需要新增菜品口味
        return false;
    }
}
