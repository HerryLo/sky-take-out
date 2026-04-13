package com.liuheng.service.impl;

import com.liuheng.constant.StatusConstant;
import com.liuheng.dto.SetmealDTO;
import com.liuheng.entity.Setmeal;
import com.liuheng.entity.SetmealDish;
import com.liuheng.mapper.SetmealDishMapper;
import com.liuheng.mapper.SetmealMapper;
import com.liuheng.service.SetmealService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SetmealServiceImpl implements SetmealService {
    private final SetmealMapper setmealMapper;
    private final SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */

    @Override
    @Transactional
    public boolean saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setStatus(StatusConstant.DISABLE); // 默认为停售状态

        setmealMapper.save(setmeal);

        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            for (SetmealDish setmeal1 : setmealDishes) {
                setmeal1.setSetmealId(setmealId);
            }
            setmealDishMapper.save(setmealDishes);
        }
        return true;
    }
}
