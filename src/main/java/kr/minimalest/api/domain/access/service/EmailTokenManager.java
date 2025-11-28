package kr.minimalest.api.domain.access.service;

import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.VerificationToken;

public interface EmailTokenManager {

    VerificationToken generateAndSaveToken(Email email);

    boolean validateAndRemoveToken(Email email, VerificationToken token);

    String refreshToken(Email email);
}
