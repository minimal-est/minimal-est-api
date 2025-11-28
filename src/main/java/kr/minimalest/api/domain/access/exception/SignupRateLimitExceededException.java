package kr.minimalest.api.domain.access.exception;

import kr.minimalest.api.domain.BusinessException;

public class SignupRateLimitExceededException extends BusinessException {
    public SignupRateLimitExceededException(String message) {
        super(message);
    }
}
