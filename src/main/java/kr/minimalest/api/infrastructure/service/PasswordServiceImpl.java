package kr.minimalest.api.infrastructure.service;

import kr.minimalest.api.domain.user.Password;
import kr.minimalest.api.domain.user.service.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {

    private final PasswordEncoder encoder;

    public Password encryptPassword(String plainPassword) {
        String encrypted = encoder.encode(plainPassword);
        return Password.of(encrypted);
    }

    public boolean matches(String plainPassword, Password encryptedPassword) {
        return encoder.matches(plainPassword, encryptedPassword.value());
    }
}
