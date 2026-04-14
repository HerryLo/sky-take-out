package com.liuheng.service;

import com.liuheng.dto.SetmealDTO;
import com.liuheng.dto.SetmealPageQueryDTO;
import com.liuheng.vo.SetmealVO;
import com.liuheng.result.PageResult;
import java.util.List;

public interface SetmealService {
    boolean saveWithDish(SetmealDTO setmealDTO);

    // Phase 1 methods
    PageResult<SetmealVO> search(SetmealPageQueryDTO setmealPageQueryDTO);
    SetmealVO getById(Long id);
    List<SetmealVO> getByCategoryId(Long categoryId);
}
