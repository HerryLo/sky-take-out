package com.liuheng.interceptor;

import com.liuheng.context.BaseContext;
import com.liuheng.properties.JwtProperties;
import com.liuheng.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;
    private final RedisTemplate redisTemplate;

    private static final String REFRESH_TOKEN_KEY_PREFIX = "user:refresh:";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader(jwtProperties.getUserTokenName());

        try {
            // 1. 验证JWT签名和过期时间
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            String openid = claims.get("openid").toString();
            Long userId = Long.valueOf(claims.get("userId").toString());
            log.info("用户端token验证成功: userId={}, openid={}", userId, openid);

            // 2. 查询Redis中的Refresh Token
            String redisRefreshToken = (String) redisTemplate.opsForValue().get(REFRESH_TOKEN_KEY_PREFIX + openid);

            // 3. 比较Redis中的refresh token是否存在（说明会话有效）
            if (redisRefreshToken == null) {
                log.warn("Redis中无refresh token: openid={}", openid);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            // 4. 设置BaseContext
            BaseContext.setCurrentId(userId);
            return true;
        } catch (Exception e) {
            log.error("用户端token验证失败: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        BaseContext.clearCurrentId();
    }
}
