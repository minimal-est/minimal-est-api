package kr.minimalest.api.domain.user.exception;

import kr.minimalest.api.domain.BusinessException;

public class TokenVerificationException extends BusinessException {
    public TokenVerificationException() {
        super();
    }

    public TokenVerificationException(Throwable cause) {
        super(cause);
    }

    public TokenVerificationException(String message) {
        super(message);
    }

    public TokenVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
