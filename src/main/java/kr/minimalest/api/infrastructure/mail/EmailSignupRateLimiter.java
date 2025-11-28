package kr.minimalest.api.infrastructure.mail;

import kr.minimalest.api.domain.access.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EmailSignupRateLimiter {

    private static final String KEY_PREFIX = "rate-limit:signup:email:";
    private static final int MAX_ATTEMPTS = 5;
    private static final int WINDOW_SECONDS = 3600; // 1시간

    private final RedisTemplate<String, String> redisTemplate;

    public boolean isAllowed(Email email) {
        long now = Instant.now().getEpochSecond();
        long windowStart = now - WINDOW_SECONDS;

        // 1시간 내의 요청 수 세기
        Long count = redisTemplate.opsForZSet()
                .count(getKey(email), windowStart, now);

        cleanupOldRequests(email, windowStart);

        return count == null || count < MAX_ATTEMPTS;
    }

    public void recordAttempt(Email email) {
        long now = Instant.now().getEpochSecond();
        String requestId = UUID.randomUUID().toString();

        redisTemplate.opsForZSet()
                .add(getKey(email), requestId, now);

        redisTemplate.expire(getKey(email), Duration.ofSeconds(WINDOW_SECONDS));
    }

    private void cleanupOldRequests(Email email, long windowStart) {
        redisTemplate.opsForZSet()
                .removeRangeByScore(getKey(email), Double.NEGATIVE_INFINITY, windowStart);
    }

    private String getKey(Email email) {
        return KEY_PREFIX + email.value();
    }
}
