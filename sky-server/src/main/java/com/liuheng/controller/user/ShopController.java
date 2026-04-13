package com.liuheng.controller.user;

import com.liuheng.result.Result;
import com.liuheng.service.ShopService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Tag(name = "店铺管理", description = "店铺管理关接口")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;

    @GetMapping
    public Result getStatus() {
        Integer status = shopService.getStatus();
        return Result.success(status == 1 ? "营业中" : "已打烊");
    }
}
