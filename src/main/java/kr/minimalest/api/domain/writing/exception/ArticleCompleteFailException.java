package kr.minimalest.api.domain.writing.exception;

import kr.minimalest.api.domain.BusinessException;

public class ArticleCompleteFailException extends BusinessException {
    public ArticleCompleteFailException() {
        super();
    }

    public ArticleCompleteFailException(Throwable cause) {
        super(cause);
    }

    public ArticleCompleteFailException(String message) {
        super(message);
    }

    public ArticleCompleteFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
