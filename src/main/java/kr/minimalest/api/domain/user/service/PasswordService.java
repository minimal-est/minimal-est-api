package kr.minimalest.api.domain.user.service;

import kr.minimalest.api.domain.user.Password;

public interface PasswordService {

    Password encryptPassword(String plainPassword);

    boolean matches(String plainPassword, Password encryptedPassword);
}
