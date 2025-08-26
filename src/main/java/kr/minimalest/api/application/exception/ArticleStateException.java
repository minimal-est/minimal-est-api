package kr.minimalest.api.application.exception;

public class ArticleStateException extends BusinessException {
    public ArticleStateException() {
        super();
    }

    public ArticleStateException(Throwable cause) {
        super(cause);
    }

    public ArticleStateException(String message) {
        super(message);
    }

    public ArticleStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
