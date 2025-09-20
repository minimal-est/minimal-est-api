package kr.minimalest.api.domain.user.exception;

import kr.minimalest.api.domain.BusinessException;

public class AuthenticateUserException extends BusinessException {
    public AuthenticateUserException() {
        super();
    }

    public AuthenticateUserException(Throwable cause) {
        super(cause);
    }

    public AuthenticateUserException(String message) {
        super(message);
    }

    public AuthenticateUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
