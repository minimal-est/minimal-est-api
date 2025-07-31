package kr.minimalest.api.application.exception;

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
