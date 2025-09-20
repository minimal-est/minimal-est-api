package kr.minimalest.api.domain.user.exception;

import kr.minimalest.api.domain.BusinessException;

public class InvalidRefreshToken extends BusinessException {
    public InvalidRefreshToken() {
        super();
    }

    public InvalidRefreshToken(Throwable cause) {
        super(cause);
    }

    public InvalidRefreshToken(String message) {
        super(message);
    }

    public InvalidRefreshToken(String message, Throwable cause) {
        super(message, cause);
    }
}
