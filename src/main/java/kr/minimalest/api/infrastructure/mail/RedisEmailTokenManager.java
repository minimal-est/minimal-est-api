package kr.minimalest.api.infrastructure.mail;

import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.VerificationToken;
import kr.minimalest.api.domain.access.service.EmailTokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisEmailTokenManager implements EmailTokenManager {
    private final RedisTemplate<String, String> redisTemplate;
    private final MailConfig mailConfig;

    @Override
    public VerificationToken generateAndSaveToken(Email email) {
        VerificationToken token = VerificationToken.generate();
        String redisKey = mailConfig.getEmailTokenPrefix() + email.value();

        redisTemplate.opsForValue().set(
                redisKey,
                token.uuid().toString(),
                Duration.ofHours(mailConfig.getTokenValidityHours())
        );

        String tokenValue = redisTemplate.opsForValue().get(redisKey);
        log.info("이메일 인증 토큰 저장: {}, {}", email.value(), tokenValue);

        return token;
    }

    @Override
    public boolean validateAndRemoveToken(Email email, VerificationToken token) {
        String redisKey = mailConfig.getEmailTokenPrefix() + email.value();
        String storedToken = redisTemplate.opsForValue().get(redisKey);

        if (storedToken == null) {
            log.warn("이메일 인증 토큰 만료: {}", email.value());
            return false;
        }

        if (!storedToken.equals(token.uuid().toString())) {
            log.warn("유효하지 않은 이메일 인증 토큰: {}", email.value());
            return false;
        }

        redisTemplate.delete(redisKey);
        log.info("이메일 인증 토큰 검증 및 삭제: {}", email.value());
        return true;
    }

    @Override
    public String refreshToken(Email email) {
        String redisKey = mailConfig.getEmailTokenPrefix() + email.value();
        redisTemplate.delete(redisKey);

        VerificationToken newToken = generateAndSaveToken(email);
        log.info("인증 토큰 재발급: {}", email.value());
        return newToken.uuid().toString();
    }
}
