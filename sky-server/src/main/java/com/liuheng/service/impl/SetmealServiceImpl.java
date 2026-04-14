package com.liuheng.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liuheng.constant.StatusConstant;
import com.liuheng.dto.SetmealDTO;
import com.liuheng.dto.SetmealPageQueryDTO;
import com.liuheng.entity.Category;
import com.liuheng.entity.Setmeal;
import com.liuheng.entity.SetmealDish;
import com.liuheng.mapper.CategoryMapper;
import com.liuheng.mapper.SetmealDishMapper;
import com.liuheng.mapper.SetmealMapper;
import com.liuheng.service.SetmealService;
import com.liuheng.vo.SetmealVO;
import com.liuheng.result.PageResult;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SetmealServiceImpl implements SetmealService {
    private final SetmealMapper setmealMapper;
    private final SetmealDishMapper setmealDishMapper;
    private final CategoryMapper categoryMapper;

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

    @Override
    public PageResult search(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询套餐：{}", setmealPageQueryDTO);
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        List<Setmeal> setmealList = setmealMapper.list(setmealPageQueryDTO);
        List<SetmealVO> setmealVOList = new ArrayList<>();

        for (Setmeal setmeal : setmealList) {
            SetmealVO setmealVO = new SetmealVO();
            BeanUtils.copyProperties(setmeal, setmealVO);

            // Get category name
            Category category = categoryMapper.getById(setmeal.getCategoryId());
            setmealVO.setCategoryName(category != null ? category.getName() : "Unknown Category");

            // Get associated dishes
            List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(setmeal.getId());
            setmealVO.setSetmealDishes(setmealDishes);

            setmealVOList.add(setmealVO);
        }

        return new PageResult(PageInfo.of(setmealList).getTotal(), setmealVOList);
    }

    @Override
    public SetmealVO getById(Long id) {
        // TODO: implement Phase 1
        return null;
    }

    @Override
    public List<SetmealVO> getByCategoryId(Long categoryId) {
        // TODO: implement Phase 1
        return null;
    }
}
