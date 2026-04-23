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
import com.liuheng.vo.DishItemVO;
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
        log.info("根据ID查询套餐：{}", id);
        if (id == null) {
            throw new IllegalArgumentException("套餐ID不能为空");
        }

        // Get setmeal basic info
        Setmeal setmeal = setmealMapper.getById(id);
        if (setmeal == null) {
            return null;
        }

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);

        // Get category name
        Category category = categoryMapper.getById(setmeal.getCategoryId());
        setmealVO.setCategoryName(category != null ? category.getName() : "Unknown Category");

        // Get associated dishes
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    @Override
    public List<SetmealVO> getByCategoryId(Long categoryId) {
        log.info("根据分类ID查询套餐：{}", categoryId);

        List<Setmeal> setmealList = setmealMapper.getByCategoryId(categoryId);
        List<SetmealVO> setmealVOList = new ArrayList<>();

        for (Setmeal setmeal : setmealList) {
            SetmealVO setmealVO = new SetmealVO();
            BeanUtils.copyProperties(setmeal, setmealVO);

            // Get category name
            Category category = categoryMapper.getById(setmeal.getCategoryId());
            setmealVO.setCategoryName(category != null ? category.getName() : "Unknown Category");

            setmealVOList.add(setmealVO);
        }

        return setmealVOList;
    }

    @Override
    public boolean updateWithDish(SetmealDTO setmealDTO) {
        log.info("修改套餐：{}", setmealDTO);

        // Update setmeal basic info
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        // Delete old setmeal dishes
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());

        // Save new setmeal dishes
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmealDTO.getId());
            }
            setmealDishMapper.save(setmealDishes);
        }

        return true;
    }

    @Override
    public boolean delete(Long id) {
        log.info("删除套餐：{}", id);

        // Check if setmeal exists
        Setmeal setmeal = setmealMapper.getById(id);
        if (setmeal == null) {
            throw new IllegalArgumentException("套餐不存在");
        }

        // Delete associated dishes
        setmealDishMapper.deleteBySetmealId(id);

        // Delete setmeal
        setmealMapper.deleteById(id);

        return true;
    }

    @Override
    public boolean changeStatus(Integer status, Long id) {
        log.info("启用/禁用套餐：status={}, id={}", status, id);

        // Check if setmeal exists
        Setmeal setmeal = setmealMapper.getById(id);
        if (setmeal == null) {
            throw new IllegalArgumentException("套餐不存在");
        }

        // Update status
        setmeal.setStatus(status);
        setmealMapper.update(setmeal);

        return true;
    }

    @Override
    public List<DishItemVO> getDishById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
