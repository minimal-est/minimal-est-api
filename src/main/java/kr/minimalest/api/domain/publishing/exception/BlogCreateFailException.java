package kr.minimalest.api.domain.publishing.exception;

public class BlogCreateFailException extends RuntimeException{
    public BlogCreateFailException() {
        super();
    }

    public BlogCreateFailException(String message) {
        super(message);
    }

    public BlogCreateFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlogCreateFailException(Throwable cause) {
        super(cause);
    }
}
