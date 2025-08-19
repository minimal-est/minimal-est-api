package kr.minimalest.api.application.user;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.application.exception.EmailDuplicatedException;
import kr.minimalest.api.domain.user.Email;
import kr.minimalest.api.domain.user.Password;
import kr.minimalest.api.domain.user.User;
import kr.minimalest.api.domain.user.UserId;
import kr.minimalest.api.domain.user.repository.UserRepository;
import kr.minimalest.api.domain.user.service.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Business
@RequiredArgsConstructor
public class SignUp {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public SignUpResult exec(SignUpArgument argument) {
        Email email = Email.of(argument.email());
        Password encryptedPassword = passwordService.encryptPassword(argument.password());

        validateEmail(email);

        User newUser = User.signUp(email, encryptedPassword);
        UserId savedUserId = userRepository.save(newUser);

        publishEvents(newUser);

        return SignUpResult.of(savedUserId);
    }

    private void validateEmail(Email email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailDuplicatedException("이미 사용 중인 이메일입니다.");
        }
    }

    private void publishEvents(User user) {
        user.releaseEvents().forEach(eventPublisher::publishEvent);
    }
}
