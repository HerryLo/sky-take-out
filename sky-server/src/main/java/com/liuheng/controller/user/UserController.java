package com.liuheng.controller.user;

import com.liuheng.context.BaseContext;
import com.liuheng.dto.UserLoginDTO;
import com.liuheng.entity.User;
import com.liuheng.mapper.UserMapper;
import com.liuheng.properties.JwtProperties;
import com.liuheng.result.Result;
import com.liuheng.service.UserService;
import com.liuheng.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController("userUserController")
@RequestMapping("/user/user")
@Tag(name = "用户", description = "用户登录注册登出接口")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtProperties jwtProperties;

    @PostMapping("/login")
    @Operation(summary = "登录注册")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        UserLoginVO userLoginVO = userService.login(userLoginDTO);
        return Result.success(userLoginVO);
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新Token")
    public Result<UserLoginVO> refresh(@RequestHeader("X-Refresh-Token") String refreshToken) {
        UserLoginVO userLoginVO = userService.refresh(refreshToken);
        return Result.success(userLoginVO);
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    public Result<String> logout(@RequestHeader("token") String token) {
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);
        if (user != null) {
            userService.logout(user.getOpenid());
        }
        return Result.success("登出成功");
    }
}
