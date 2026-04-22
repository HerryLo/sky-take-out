package com.liuheng.service.impl;

import com.liuheng.dto.UserLoginDTO;
import com.liuheng.entity.User;
import com.liuheng.mapper.UserMapper;
import com.liuheng.properties.JwtProperties;
import com.liuheng.properties.WeChatProperties;
import com.liuheng.vo.UserLoginVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtProperties jwtProperties;

    @Mock
    private WeChatProperties weChatProperties;

    @Mock
    private RedisTemplate redisTemplate;

    @Mock
    private ValueOperations valueOperations;

    @InjectMocks
    private UserServiceImpl userService;

    private UserLoginDTO userLoginDTO;

    @BeforeEach
    void setUp() {
        userLoginDTO = new UserLoginDTO();
        userLoginDTO.setLoginCode("test_login_code");
        userLoginDTO.setPhoneCode("test_phone_code");
    }

    @Test
    void logout_shouldDeleteRedisKeys() {
        // Given
        String openid = "test_openid";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn("refresh_token_value");
        when(redisTemplate.delete(anyString())).thenReturn(true);

        // When
        userService.logout(openid);

        // Then
        verify(redisTemplate, times(2)).delete(anyString());
    }

    @Test
    void logout_withNoRefreshToken_shouldOnlyDeleteMainKey() {
        // Given
        String openid = "test_openid";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn(null);
        when(redisTemplate.delete(anyString())).thenReturn(true);

        // When
        userService.logout(openid);

        // Then
        verify(redisTemplate, times(1)).delete("user:refresh:" + openid);
    }
}
