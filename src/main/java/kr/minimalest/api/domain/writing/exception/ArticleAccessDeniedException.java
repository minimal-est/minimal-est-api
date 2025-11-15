package kr.minimalest.api.domain.writing.exception;

public class ArticleAccessDeniedException extends RuntimeException{
    public ArticleAccessDeniedException() {
        super();
    }

    public ArticleAccessDeniedException(Throwable cause) {
        super(cause);
    }

    public ArticleAccessDeniedException(String message) {
        super(message);
    }

    public ArticleAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
