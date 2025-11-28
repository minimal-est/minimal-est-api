package kr.minimalest.api.domain.access.service;

import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.VerificationToken;

public interface EmailVerificationService {

    void sendVerificationEmail(Email email);

    void resendVerificationEmail(Email email);

    boolean verifyEmail(Email email, VerificationToken token);
}
