package com.liuheng.controller.admin;

import com.liuheng.result.Result;
import com.liuheng.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Tag(name = "店铺管理", description = "店铺管理关接口")
@RequiredArgsConstructor
public class ShopController {
    private final ShopService shopService;

    /**
     * 修改店铺状态
     * @param status 1营业中，2已打烊
     * @return
     */
    @Operation(summary = "更新店铺状态")
    @PostMapping
    public Result setStatus(Integer status) {
        boolean bool = shopService.setStatus(status);
        return Result.success(bool);
    }

    /**
     * 获取店铺状态
     * @return
     */
    @Operation(summary = "获取店铺状态")
    @GetMapping
    public Result getStatus() {
        Integer status = shopService.getStatus();
        return Result.success(status == 1 ? "营业中" : "已打烊");
    }
}
