package kr.minimalest.api.application.exception;

public class PasswordNotEncodedException extends BusinessException {
    public PasswordNotEncodedException() {
        super();
    }

    public PasswordNotEncodedException(Throwable cause) {
        super(cause);
    }

    public PasswordNotEncodedException(String message) {
        super(message);
    }

    public PasswordNotEncodedException(String message, Throwable cause) {
        super(message, cause);
    }
}
