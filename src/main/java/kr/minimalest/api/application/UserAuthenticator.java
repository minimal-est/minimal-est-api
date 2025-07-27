package kr.minimalest.api.application;

import kr.minimalest.api.domain.user.Email;
import kr.minimalest.api.domain.user.Password;
import kr.minimalest.api.domain.user.User;

public interface UserAuthenticator {
    User authenticate(Email email, Password password);
}
