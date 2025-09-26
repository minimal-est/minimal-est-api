package kr.minimalest.api.domain.publishing.exception;

import kr.minimalest.api.domain.BusinessException;

public class PenNameAlreadyExists extends BusinessException {
    public PenNameAlreadyExists() {
        super();
    }

    public PenNameAlreadyExists(Throwable cause) {
        super(cause);
    }

    public PenNameAlreadyExists(String message) {
        super(message);
    }

    public PenNameAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }
}
