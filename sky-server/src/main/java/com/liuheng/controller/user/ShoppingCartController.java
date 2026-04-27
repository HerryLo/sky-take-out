package com.liuheng.controller.user;

import com.liuheng.dto.ShoppingCartDTO;
import com.liuheng.result.Result;
import com.liuheng.service.ShoppingCartService;
import com.liuheng.vo.ShoppingCartVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userShoppingCartController")
@RequestMapping("/user/shoppingCart")
@Tag(name = "购物车", description = "购物车相关接口")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @Operation(summary = "添加商品到购物车")
    public Result<String> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.add(shoppingCartDTO);
        return Result.success("添加成功");
    }

    @GetMapping("/list")
    @Operation(summary = "查看购物车列表")
    public Result<List<ShoppingCartVO>> list() {
        List<ShoppingCartVO> list = shoppingCartService.list();
        return Result.success(list);
    }

    @PostMapping("/number")
    @Operation(summary = "修改购物车商品数量")
    public Result<String> updateNumber(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.updateNumber(shoppingCartDTO);
        return Result.success("修改成功");
    }

    @DeleteMapping
    @Operation(summary = "从购物车移除商品")
    public Result<String> delete(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.delete(shoppingCartDTO);
        return Result.success("删除成功");
    }

    @DeleteMapping("/clean")
    @Operation(summary = "清空购物车")
    public Result<String> clean() {
        shoppingCartService.clean();
        return Result.success("清空成功");
    }
}