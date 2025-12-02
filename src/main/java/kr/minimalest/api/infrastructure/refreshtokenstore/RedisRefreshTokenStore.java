package kr.minimalest.api.infrastructure.refreshtokenstore;

import kr.minimalest.api.domain.access.RefreshTokenStore;
import kr.minimalest.api.domain.access.Token;
import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.infrastructure.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisRefreshTokenStore implements RefreshTokenStore {

    private static final String REFRESH_TOKEN_PREFIX = "refresh_token:";

    private final StringRedisTemplate redisTemplate;
    private final JwtProperties jwtProperties;

    @Override
    public void put(UserId userId, Token refreshToken) {
        String key = REFRESH_TOKEN_PREFIX + userId.id();
        long refreshTokenExpiry = jwtProperties.refreshValidityInSeconds();
        log.info("refresh token 저장: {}", refreshToken.value());

        // Redis에 refresh token 저장, TTL은 refresh token의 만료시간과 동일
        redisTemplate.opsForValue().set(key, refreshToken.value(), refreshTokenExpiry, TimeUnit.SECONDS);
    }

    @Override
    public Optional<Token> find(UserId userId) {
        String key = REFRESH_TOKEN_PREFIX + userId.id();
        String tokenValue = redisTemplate.opsForValue().get(key);

        if (tokenValue == null) {
            return Optional.empty();
        }

        return Optional.of(Token.of(tokenValue));
    }

    @Override
    public void remove(UserId userId) {
        String key = REFRESH_TOKEN_PREFIX + userId.id();
        redisTemplate.delete(key);
    }
}
