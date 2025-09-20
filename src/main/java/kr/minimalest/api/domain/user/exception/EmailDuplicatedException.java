package kr.minimalest.api.domain.user.exception;

import kr.minimalest.api.domain.BusinessException;

public class EmailDuplicatedException extends BusinessException {
    public EmailDuplicatedException() {
        super();
    }

    public EmailDuplicatedException(Throwable cause) {
        super(cause);
    }

    public EmailDuplicatedException(String message) {
        super(message);
    }

    public EmailDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }
}
