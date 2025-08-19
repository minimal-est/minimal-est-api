package kr.minimalest.api.domain.user.service;

import kr.minimalest.api.domain.user.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {
    private final PasswordEncoder encoder;

    public Password encryptPassword(String plainPassword) {
        String encrypted = encoder.encode(plainPassword);
        return Password.of(encrypted);
    }

    public boolean matches(String plainPassword, Password encryptedPassword) {
        return encoder.matches(plainPassword, encryptedPassword.value());
    }
}
