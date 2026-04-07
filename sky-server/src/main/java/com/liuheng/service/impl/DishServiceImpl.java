package com.liuheng.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.liuheng.dto.DishDTO;
import com.liuheng.dto.DishPageQueryDTO;
import com.liuheng.vo.DishVO;
import com.liuheng.entity.Dish;
import com.liuheng.entity.DishFlavor;
import com.liuheng.mapper.DishFlavorMapper;
import com.liuheng.mapper.DishMapper;
import com.liuheng.result.PageResult;
import com.liuheng.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
    @Transactional(rollbackFor = Exception.class)
    public boolean saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.save(dish);

        Long id = dish.getId();

        // TODO 新增菜品同时还需要新增菜品口味
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        if(dishFlavorList != null) {
            for(DishFlavor dishFlavor : dishFlavorList) {
                dishFlavor.setDishId(id);
            }
            dishFlavorMapper.save(dishFlavorList);
        }

        return true;
    }

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult search(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        List<DishVO> dishVOList = dishMapper.pageQuery(dishPageQueryDTO);
        Page<DishVO> p = (Page<DishVO>) dishVOList;

        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 根据菜品id查询菜品口味
     * @param id
     * @return
     */
    @Override
    public List<DishFlavor> getFlavorByDishId(Long id) {
        return dishFlavorMapper.getFlavorByDishId(id);
    }

    /**
     * 根据菜品id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public DishVO getById(Long categoryId) {
        return dishMapper.getById(categoryId);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        return dishMapper.getByCategoryId(categoryId);
    }

    @Override
    public boolean update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        // TODO 删除原有菜品的口味
        dishFlavorMapper.deleteByDishId(dish.getId());

        // TODO 修改品同时还需要修改菜品口味
        List<DishFlavor> dishFlavorList = dishDTO.getFlavors();
        if(dishFlavorList != null && !dishFlavorList.isEmpty()) {
            for(DishFlavor dishFlavor : dishFlavorList) {
                dishFlavor.setDishId(dish.getId());
            }
            dishFlavorMapper.save(dishFlavorList);
        }

        return true;
    }
}
