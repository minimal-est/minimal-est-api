package kr.minimalest.api.domain.publishing.exception;

public class BlogNotFoundException extends RuntimeException {

    public BlogNotFoundException() {
        super();
    }

    public BlogNotFoundException(Throwable cause) {
        super(cause);
    }

    public BlogNotFoundException(String message) {
        super(message);
    }

    public BlogNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
