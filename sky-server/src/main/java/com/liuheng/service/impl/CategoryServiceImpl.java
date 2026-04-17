package com.liuheng.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.liuheng.constant.MessageConstant;
import com.liuheng.constant.StatusConstant;
import com.liuheng.context.BaseContext;
import com.liuheng.dto.CategoryDTO;
import com.liuheng.dto.CategoryPageQueryDTO;
import com.liuheng.dto.CategoryStatusDTO;
import com.liuheng.entity.Category;
import com.liuheng.exception.AccountNotFoundException;
import com.liuheng.exception.DeletionNotAllowedException;
import com.liuheng.mapper.CategoryMapper;
import com.liuheng.mapper.DishMapper;
import com.liuheng.mapper.SetmealMapper;
import com.liuheng.result.PageResult;
import com.liuheng.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    DishMapper dishMapper;

    @Autowired
    SetmealMapper setmealMapper;

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public boolean save(CategoryDTO categoryDTO) {
        Category category = new Category();

        BeanUtils.copyProperties(categoryDTO,category);

        // 默认分类禁用
        category.setStatus(StatusConstant.DISABLE);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());

        return categoryMapper.save(category) > 0;
    }

    /**
     * 分页查询分类
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult search(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        String name = categoryPageQueryDTO.getName();
        Integer type = categoryPageQueryDTO.getType();
        List<Category> list = categoryMapper.list(name,type);
        Page<Category> p = (Page<Category>) list;

        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 更新分类
     * @param categoryDTO
     * @return
     */
    @Override
    public boolean update(CategoryDTO categoryDTO) {
        Category category = new Category();

        BeanUtils.copyProperties(categoryDTO, category);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());

        return categoryMapper.update(category) > 0;
    }

    /**
     * 开启禁用分类
     * @param categoryStatusDTO
     * @return
     */
    @Override
    public boolean updateStatus(CategoryStatusDTO categoryStatusDTO) {
        return categoryMapper.updateStatus(categoryStatusDTO.getId() , categoryStatusDTO.getStatus()) > 0;
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @Override
    public boolean delete(Long id) {
        Integer count = dishMapper.countByCategoryId(id);
        if(count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        count = setmealMapper.countByCategoryId(id);
        if(count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        Category category = categoryMapper.getById(id);
        if(category == null) {
            throw new AccountNotFoundException("分类id不存在");
        }

        return categoryMapper.deleteById(id) > 0;
    }

    /**
     *
     * @param type
     * @return
     */
    @Override
    public List<Category> list(Integer type) {
        return categoryMapper.list("", type);
    }
}
