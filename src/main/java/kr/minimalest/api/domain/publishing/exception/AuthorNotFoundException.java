package kr.minimalest.api.domain.publishing.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException() {
        super();
    }

    public AuthorNotFoundException(Throwable cause) {
        super(cause);
    }

    public AuthorNotFoundException(String message) {
        super(message);
    }

    public AuthorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
