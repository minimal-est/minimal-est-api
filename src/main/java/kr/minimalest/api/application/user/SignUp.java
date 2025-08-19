package kr.minimalest.api.application.user;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.application.exception.EmailDuplicatedException;
import kr.minimalest.api.application.exception.PasswordNotEncodedException;
import kr.minimalest.api.domain.user.Email;
import kr.minimalest.api.domain.user.Password;
import kr.minimalest.api.domain.user.User;
import kr.minimalest.api.domain.user.UserId;
import kr.minimalest.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class SignUp {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public SignUpResult exec(SignUpArgument argument) {
        Email email = Email.of(argument.email());
        Password rawPassword = Password.of(argument.password());
        Password encPassword = Password.of(encoder.encode(argument.password()));

        validateEmail(email);
        validatePassword(rawPassword, encPassword);

        User newUser = User.signUp(email, encPassword);
        UserId savedUserId = userRepository.save(newUser);

        return SignUpResult.of(savedUserId);
    }

    private void validateEmail(Email email) {
        boolean isEmailDuplicated = userRepository.existsByEmail(email);
        if (isEmailDuplicated) {
            throw new EmailDuplicatedException("이메일이 중복됩니다.");
        }
    }

    private void validatePassword(Password from, Password to) {
        boolean isPasswordEncoded = encoder.matches(from.value(), to.value());
        if (!isPasswordEncoded) {
            throw new PasswordNotEncodedException("비밀번호는 인코딩(암호화)되어야 합니다.");
        }
    }
}
