package kr.minimalest.api.domain.writing.exception;

import kr.minimalest.api.domain.BusinessException;

public class ArticleNotFoundException extends BusinessException {
    public ArticleNotFoundException() {
        super();
    }

    public ArticleNotFoundException(Throwable cause) {
        super(cause);
    }

    public ArticleNotFoundException(String message) {
        super(message);
    }

    public ArticleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
