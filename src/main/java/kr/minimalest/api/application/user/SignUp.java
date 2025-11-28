package kr.minimalest.api.application.user;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.Password;
import kr.minimalest.api.domain.access.PendingSignup;
import kr.minimalest.api.domain.access.exception.EmailDuplicatedException;
import kr.minimalest.api.domain.access.exception.SignupRateLimitExceededException;
import kr.minimalest.api.domain.access.repository.PendingSignupRepository;
import kr.minimalest.api.domain.access.repository.UserRepository;
import kr.minimalest.api.domain.access.service.EmailVerificationService;
import kr.minimalest.api.domain.access.service.PasswordService;
import kr.minimalest.api.infrastructure.mail.EmailSignupRateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Business
@RequiredArgsConstructor
public class SignUp {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final EmailVerificationService emailVerificationService;
    private final PendingSignupRepository pendingSignupRepository;
    private final EmailSignupRateLimiter emailSignupRateLimiter;

    @Transactional
    public void exec(SignUpArgument argument) {
        Email email = Email.of(argument.email());

        // Rate Limit 확인
        if (!emailSignupRateLimiter.isAllowed(email)) {
            throw new SignupRateLimitExceededException(
                "1시간 내에 5회 이상 시도했습니다. 잠시 후 다시 시도하세요."
            );
        }

        // 시도 기록
        emailSignupRateLimiter.recordAttempt(email);

        Password encryptedPassword = passwordService.encryptPassword(argument.password());

        validateEmail(email);

        PendingSignup pendingSignup = PendingSignup.of(email, encryptedPassword);
        pendingSignupRepository.save(pendingSignup);

        emailVerificationService.sendVerificationEmail(email);

        log.info("이메일 인증 회원가입 신청: {}", email.value());
    }

    private void validateEmail(Email email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailDuplicatedException("이미 사용 중인 이메일입니다.");
        }
    }
}
