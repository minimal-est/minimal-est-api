package kr.minimalest.api.domain.access.service;

import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.Password;
import kr.minimalest.api.domain.access.User;

public interface UserAuthenticator {
    User authenticate(Email email, Password password);
}
