package kr.minimalest.api.application.user;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.BusinessException;
import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.PendingSignup;
import kr.minimalest.api.domain.access.User;
import kr.minimalest.api.domain.access.VerificationToken;
import kr.minimalest.api.domain.access.repository.PendingSignupRepository;
import kr.minimalest.api.domain.access.repository.UserRepository;
import kr.minimalest.api.domain.access.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Business
@RequiredArgsConstructor
public class VerifyEmail {

    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;
    private final PendingSignupRepository pendingSignupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void exec(VerifyEmailArgument argument) {
        Email email = Email.of(argument.email());
        VerificationToken token = new VerificationToken(UUID.fromString(argument.token()));

        // 토큰 검증
        boolean isValid = emailVerificationService.verifyEmail(email, token);
        if (!isValid) {
            throw new BusinessException("유효하지 않은 인증 토큰입니다.");
        }

        // 임시 회원가입 정보 조회
        PendingSignup pendingSignup = pendingSignupRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("회원가입 정보를 찾을 수 없습니다"));

        // User 생성
        User newUser = User.signUp(email, pendingSignup.encryptedPassword());
        newUser.verifyEmail();
        userRepository.save(newUser);

        // 임시 정보 삭제
        pendingSignupRepository.delete(email);

        // 이벤트 발행
        publishEvents(newUser);

        log.info("이메일 인증 및 가입 완료: {}", email.value());
    }

    private void publishEvents(User user) {
        user.releaseEvents().forEach(applicationEventPublisher::publishEvent);
    }
}
