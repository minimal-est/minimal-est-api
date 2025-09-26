package kr.minimalest.api.application.user;

import kr.minimalest.api.domain.access.exception.EmailDuplicatedException;
import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.Password;
import kr.minimalest.api.domain.access.User;
import kr.minimalest.api.domain.access.event.UserSignedUpEvent;
import kr.minimalest.api.domain.access.repository.UserRepository;
import kr.minimalest.api.domain.access.service.PasswordService;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SignUpTest {

    @InjectMocks
    SignUp signUp;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordService passwordService;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Getter
    static class SignUpFixture {
        private final Email emailToSignUp = Email.of("test@test.com");
        private final String plainPassword = "test1234";
        private final Password encryptedPassword = Password.of("encoded-test1234");

        public SignUpArgument getSignUpArgument() {
            return SignUpArgument.of(emailToSignUp.value(), plainPassword);
        }
    }

    @Nested
    @DisplayName("회원가입 시")
    class HappySignUp {

        SignUpFixture fixture = new SignUpFixture();
        User userToSignUp = User.signUp(fixture.emailToSignUp, fixture.encryptedPassword);
        SignUpArgument argument = fixture.getSignUpArgument();

        @Test
        @DisplayName("DB에 사용자가 성공적으로 저장된다")
        void shouldSaveUserInDbSuccessfully() {
            // given
            given(passwordService.encryptPassword(fixture.plainPassword)).willReturn(fixture.encryptedPassword);
            given(userRepository.existsByEmail(fixture.emailToSignUp)).willReturn(false);
            given(userRepository.save(any())).willReturn(userToSignUp.getId());

            // when
            SignUpResult result = signUp.exec(argument);

            // then
            assertThat(result.userId()).isEqualTo(userToSignUp.getId());
            verify(eventPublisher, times(1)).publishEvent(any(UserSignedUpEvent.class));
        }

        @Test
        @DisplayName("이메일이 이미 존재한다면 예외가 발생한다")
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            // given
            given(userRepository.existsByEmail(fixture.emailToSignUp)).willReturn(true);

            // when & then
            assertThrows(EmailDuplicatedException.class, () -> {
                signUp.exec(argument);
            });
        }
    }
}
