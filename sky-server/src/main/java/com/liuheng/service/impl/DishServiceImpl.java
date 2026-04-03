package com.liuheng.service.impl;

import com.liuheng.context.BaseContext;
import com.liuheng.dto.DishDTO;
import com.liuheng.entity.Dish;
import com.liuheng.entity.DishFlavor;
import com.liuheng.mapper.DishFlavorMapper;
import com.liuheng.mapper.DishMapper;
import com.liuheng.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @Override
    public boolean saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setCreateTime(LocalDateTime.now());
        dish.setUpdateTime(LocalDateTime.now());
        dish.setCreateUser(BaseContext.getCurrentId());
        dish.setUpdateUser(BaseContext.getCurrentId());

        dishMapper.save(dish);

        Long id = dish.getId();

        // TODO 新增菜品同时还需要新增菜品口味
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        if(dishFlavorList != null) {
            for(DishFlavor dishFlavor : dishFlavorList) {
                dishFlavor.setDishId(id);
                dishFlavor.setCreateTime(LocalDateTime.now());
                dishFlavor.setUpdateTime(LocalDateTime.now());
                dishFlavor.setCreateUser(BaseContext.getCurrentId());
                dishFlavor.setUpdateUser(BaseContext.getCurrentId());
            }
            dishFlavorMapper.save(dishFlavorList);
        }

        return false;
    }
}
