package com.liuheng.service.impl;

import com.liuheng.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final RedisTemplate redisTemplate;

    public final String KEY = "SHOP_STATUS";

    /**
     * 修改店铺状态，1营业，2打烊
     * @param status
     * @return
     */
    @Override
    public boolean setStatus(Integer status) {
        redisTemplate.opsForValue().set(KEY, status);
        return true;
    }

    /**
     * 获取店铺状态
     * @return
     */
    @Override
    public Integer getStatus() {
        return (Integer) redisTemplate.opsForValue().get(KEY);
    }
}
