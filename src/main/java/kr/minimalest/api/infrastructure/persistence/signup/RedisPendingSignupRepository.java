package kr.minimalest.api.infrastructure.persistence.signup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.minimalest.api.domain.BusinessException;
import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.Password;
import kr.minimalest.api.domain.access.PendingSignup;
import kr.minimalest.api.domain.access.repository.PendingSignupRepository;
import kr.minimalest.api.infrastructure.mail.MailConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisPendingSignupRepository implements PendingSignupRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final MailConfig mailConfig;

    private static final String PENDING_SIGNUP_PREFIX = "signup:pending:";

    @Override
    public void save(PendingSignup pendingSignup) {
        try {
            String key = PENDING_SIGNUP_PREFIX + pendingSignup.email().value();
            String json = objectMapper.writeValueAsString(
                    new PendingSignupData(
                            pendingSignup.email().value(),
                            pendingSignup.encryptedPassword().value()
                    )
            );

            redisTemplate.opsForValue().set(
                    key,
                    json,
                    Duration.ofHours(mailConfig.getTokenValidityHours())
            );

            log.debug("임시 회원가입 정보 저장: {}", pendingSignup.email().value());
        } catch (JsonProcessingException e) {
            throw new BusinessException("가입 정보를 직렬화하는 과정 중에 오류가 발생했습니다", e);
        }
    }

    @Override
    public Optional<PendingSignup> findByEmail(Email email) {
        String key = PENDING_SIGNUP_PREFIX + email.value();
        String json = redisTemplate.opsForValue().get(key);

        if (json == null) {
            log.warn("임시 회원가입 정보를 찾을 수 없음: {}", email.value());
            return Optional.empty();
        }

        try {
            PendingSignupData data = objectMapper.readValue(json, PendingSignupData.class);
            PendingSignup pendingSignup = new PendingSignup(
                    Email.of(data.email()),
                    new Password(data.encryptedPassword())
            );
            return Optional.of(pendingSignup);
        } catch (JsonProcessingException e) {
            throw new BusinessException("가입 정보를 역직렬화하는 과정 중에 오류가 발생했습니다", e);
        }
    }

    @Override
    public void delete(Email email) {
        String key = PENDING_SIGNUP_PREFIX + email.value();
        Boolean deleted = redisTemplate.delete(key);
        log.debug("임시 회원가입 정보 삭제: {} ({})", email.value(), deleted ? "성공" : "실패");
    }

    private record PendingSignupData(String email, String encryptedPassword) {}
}
