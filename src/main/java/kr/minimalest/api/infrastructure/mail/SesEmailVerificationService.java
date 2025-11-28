package kr.minimalest.api.infrastructure.mail;

import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.VerificationToken;
import kr.minimalest.api.domain.access.service.EmailTokenManager;
import kr.minimalest.api.domain.access.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SesEmailVerificationService implements EmailVerificationService {
    private final EmailTokenManager emailTokenManager;
    private final SesAsyncEmailSender sesAsyncEmailSender;

    @Override
    public void sendVerificationEmail(Email email) {
        VerificationToken token = emailTokenManager.generateAndSaveToken(email);
        sesAsyncEmailSender.sendVerificationEmail(email, token);
    }

    @Override
    public void resendVerificationEmail(Email email) {
        String tokenString = emailTokenManager.refreshToken(email);
        VerificationToken token = new VerificationToken(UUID.fromString(tokenString));
        sesAsyncEmailSender.sendVerificationEmail(email, token);
    }

    @Override
    public boolean verifyEmail(Email email, VerificationToken token) {
        return emailTokenManager.validateAndRemoveToken(email, token);
    }
}
