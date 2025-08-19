package kr.minimalest.api.application.user;

import kr.minimalest.api.application.exception.EmailDuplicatedException;
import kr.minimalest.api.application.exception.PasswordNotEncodedException;
import kr.minimalest.api.domain.user.Email;
import kr.minimalest.api.domain.user.Password;
import kr.minimalest.api.domain.user.User;
import kr.minimalest.api.domain.user.repository.UserRepository;
import lombok.Getter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SignUpTest {

    @InjectMocks
    SignUp signUp;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder encoder;

    @Getter
    static class SignUpFixture {
        private final Email emailToSignUp = Email.of("test@test.com");
        private final Password rawPasswordToSignUp = Password.of("test1234");
        private final Password encPasswordToSignUp = Password.of("encoded-test1234");

        public SignUpArgument getSignUpArgument() {
            return SignUpArgument.of(emailToSignUp.value(), rawPasswordToSignUp.value());
        }
    }

    @Nested
    @DisplayName("회원가입 시")
    class HappySignUp {

        SignUpFixture fixture = new SignUpFixture();
        User userToSignUp = User.signUp(fixture.emailToSignUp, fixture.rawPasswordToSignUp);
        SignUpArgument argument = fixture.getSignUpArgument();

        @Test
        @DisplayName("DB에 사용자가 성공적으로 저장된다")
        void shouldSaveUserInDbSuccessfully() {
            // given
            given(userRepository.existsByEmail(fixture.emailToSignUp)).willReturn(false);
            given(userRepository.save(any())).willReturn(userToSignUp.getId());
            given(encoder.encode(fixture.rawPasswordToSignUp.value())).willReturn(fixture.encPasswordToSignUp.value());
            given(encoder.matches(
                    fixture.rawPasswordToSignUp.value(),
                    fixture.encPasswordToSignUp.value()
            )).willReturn(true);

            // when
            SignUpResult result = signUp.exec(argument);

            // then
            assertThat(result.userId()).isEqualTo(userToSignUp.getId());
        }

        @Test
        @DisplayName("이메일이 이미 존재한다면 예외가 발생한다")
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            // given
            given(userRepository.existsByEmail(fixture.emailToSignUp)).willReturn(true);
            given(encoder.encode(fixture.rawPasswordToSignUp.value())).willReturn(fixture.encPasswordToSignUp.value());

            // when & then
            assertThrows(EmailDuplicatedException.class, () -> {
                signUp.exec(argument);
            });
        }

        @Test
        @DisplayName("암호화가 정상적으로 이루어지지 않으면 예외가 발생한다")
        void shouldThrowExceptionWhenPasswordIsNotEncoded() {
            // given
            given(userRepository.existsByEmail(fixture.emailToSignUp)).willReturn(false);
            given(encoder.encode(fixture.rawPasswordToSignUp.value())).willReturn(fixture.encPasswordToSignUp.value());
            given(encoder.matches(
                    fixture.rawPasswordToSignUp.value(),
                    fixture.encPasswordToSignUp.value()
            )).willReturn(false);

            // when & then
            assertThrows(PasswordNotEncodedException.class, () -> {
                signUp.exec(argument);
            });
        }
    }
}
