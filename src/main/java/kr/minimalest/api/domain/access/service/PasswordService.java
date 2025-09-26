package kr.minimalest.api.domain.access.service;

import kr.minimalest.api.domain.access.Password;

public interface PasswordService {

    Password encryptPassword(String plainPassword);

    boolean matches(String plainPassword, Password encryptedPassword);
}
