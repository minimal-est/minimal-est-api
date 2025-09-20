package kr.minimalest.api.domain.blog.exception;

import kr.minimalest.api.domain.BusinessException;

public class UserAlreadyHasBlogException extends BusinessException {
    public UserAlreadyHasBlogException() {
        super();
    }

    public UserAlreadyHasBlogException(Throwable cause) {
        super(cause);
    }

    public UserAlreadyHasBlogException(String message) {
        super(message);
    }

    public UserAlreadyHasBlogException(String message, Throwable cause) {
        super(message, cause);
    }
}
