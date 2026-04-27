package com.liuheng.service;

import com.liuheng.dto.ShoppingCartDTO;
import com.liuheng.vo.ShoppingCartVO;
import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加商品到购物车
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车列表
     */
    List<ShoppingCartVO> list();

    /**
     * 修改购物车商品数量
     */
    void updateNumber(ShoppingCartDTO shoppingCartDTO);

    /**
     * 删除购物车商品
     */
    void delete(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     */
    void clean();
}