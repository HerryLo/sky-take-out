package com.liuheng.controller.admin;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "菜品管理", description = "菜品相关接口")
@SecurityRequirement(name = "tokenAuth")
public class DishController {
}
