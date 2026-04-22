package com.liuheng.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.liuheng.dto.UserLoginDTO;
import com.liuheng.entity.User;
import com.liuheng.mapper.UserMapper;
import com.liuheng.properties.JwtProperties;
import com.liuheng.properties.WeChatProperties;
import com.liuheng.service.UserService;
import com.liuheng.utils.JwtUtil;
import com.liuheng.utils.WeChatUtil;
import com.liuheng.vo.UserLoginVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtProperties jwtProperties;
    private final WeChatProperties weChatProperties;
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String REFRESH_TOKEN_KEY_PREFIX = "user:refresh:";
    private static final String REFRESH_TOKEN_MAPPING_PREFIX = "user:refresh:token:";
    private static final long ACCESS_TOKEN_TTL = 30 * 60 * 1000L;
    private static final long REFRESH_TOKEN_TTL = 7 * 24 * 60 * 60 * 1000L;
    private static final String DEFAULT_AVATAR = "https://default-avatar.png";

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        try {
            // 1. 调用微信code2Session获取openid和session_key
            String sessionInfo = WeChatUtil.code2Session(
                    weChatProperties.getAppId(),
                    weChatProperties.getAppSecret(),
                    userLoginDTO.getLoginCode()
            );
            JsonNode sessionNode = objectMapper.readTree(sessionInfo);
            String openid = sessionNode.get("openid").asText();
            String sessionKey = sessionNode.get("session_key").asText();

            // 2. 解密手机号
            String phoneJson = WeChatUtil.decryptPhoneNumber(sessionKey, userLoginDTO.getPhoneCode(), "");
            JsonNode phoneNode = objectMapper.readTree(phoneJson);
            String phone = phoneNode.get("phoneNumber").asText();

            // 3. 查询或创建用户
            User user = userMapper.getByOpenid(openid);
            boolean isNewUser = false;
            if (user == null) {
                user = User.builder()
                        .openid(openid)
                        .name("sb" + generateRandomString(6))
                        .phone(phone)
                        .avatar(DEFAULT_AVATAR)
                        .build();
                userMapper.save(user);
                isNewUser = true;
            } else {
                user.setPhone(phone);
            }
            log.info("用户{}: openid={}", isNewUser ? "注册" : "登录", openid);

            // 4. 生成Access Token (JWT)
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("openid", openid);
            String accessToken = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), ACCESS_TOKEN_TTL, claims);

            // 5. 生成Refresh Token (UUID)
            String refreshToken = UUID.randomUUID().toString();

            // 6. 存入Redis
            redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY_PREFIX + openid, refreshToken, REFRESH_TOKEN_TTL, TimeUnit.MILLISECONDS);
            redisTemplate.opsForValue().set(REFRESH_TOKEN_MAPPING_PREFIX + refreshToken, openid, REFRESH_TOKEN_TTL, TimeUnit.MILLISECONDS);

            // 7. 返回用户信息和token
            return UserLoginVO.builder()
                    .id(user.getId())
                    .openid(openid)
                    .name(user.getName())
                    .phone(user.getPhone())
                    .avatar(user.getAvatar())
                    .token(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (Exception e) {
            log.error("登录失败: {}", e.getMessage());
            throw new RuntimeException("登录失败: " + e.getMessage());
        }
    }

    @Override
    public UserLoginVO refresh(String refreshToken) {
        try {
            // 1. 通过refreshToken查找openid
            String openid = (String) redisTemplate.opsForValue().get(REFRESH_TOKEN_MAPPING_PREFIX + refreshToken);
            if (openid == null) {
                throw new RuntimeException("Refresh token无效或已过期");
            }

            // 2. 验证Redis中的refresh token是否匹配
            String storedRefreshToken = (String) redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY_PREFIX + openid);
            if (!refreshToken.equals(storedRefreshToken)) {
                throw new RuntimeException("Refresh token无效");
            }

            // 3. 查询用户
            User user = userMapper.getByOpenid(openid);
            if (user == null) {
                throw new RuntimeException("用户不存在");
            }

            // 4. 生成新的Access Token
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getId());
            claims.put("openid", openid);
            String accessToken = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), ACCESS_TOKEN_TTL, claims);

            // 5. 生成新的Refresh Token
            String newRefreshToken = UUID.randomUUID().toString();

            // 6. 更新Redis
            redisTemplate.opsForValue().set(REFRESH_TOKEN_KEY_PREFIX + openid, newRefreshToken, REFRESH_TOKEN_TTL, TimeUnit.MILLISECONDS);
            redisTemplate.opsForValue().set(REFRESH_TOKEN_MAPPING_PREFIX + newRefreshToken, openid, REFRESH_TOKEN_TTL, TimeUnit.MILLISECONDS);
            // 删除旧的refresh token映射
            redisTemplate.delete(REFRESH_TOKEN_MAPPING_PREFIX + refreshToken);

            // 7. 返回新token和用户信息
            return UserLoginVO.builder()
                    .token(accessToken)
                    .refreshToken(newRefreshToken)
                    .user(UserLoginVO.UserInfo.builder()
                            .id(user.getId())
                            .openid(user.getOpenid())
                            .name(user.getName())
                            .phone(user.getPhone())
                            .avatar(user.getAvatar())
                            .build())
                    .build();
        } catch (Exception e) {
            log.error("刷新token失败: {}", e.getMessage());
            throw new RuntimeException("刷新失败: " + e.getMessage());
        }
    }

    @Override
    public void logout(String openid) {
        // 获取当前refresh token以删除映射
        String refreshToken = (String) redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY_PREFIX + openid);
        if (refreshToken != null) {
            redisTemplate.delete(REFRESH_TOKEN_MAPPING_PREFIX + refreshToken);
        }
        redisTemplate.delete(REFRESH_TOKEN_KEY_PREFIX + openid);
        log.info("用户登出: openid={}", openid);
    }

    private String generateRandomString(int length) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
}
