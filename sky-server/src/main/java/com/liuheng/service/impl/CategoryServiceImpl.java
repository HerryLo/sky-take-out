package com.liuheng.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.liuheng.constant.StatusConstant;
import com.liuheng.context.BaseContext;
import com.liuheng.dto.CategoryDTO;
import com.liuheng.dto.CategoryPageQueryDTO;
import com.liuheng.entity.Category;
import com.liuheng.mapper.CategoryMapper;
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

        List<Category> list = categoryMapper.list(categoryPageQueryDTO);
        Page<Category> p = (Page<Category>) list;

        return new PageResult(p.getTotal(), p.getResult());
    }
}
