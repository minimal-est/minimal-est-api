package kr.minimalest.api.application.exception;

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
