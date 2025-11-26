package kr.minimalest.api.domain.publishing.exception;

public class InvalidPenNameException extends RuntimeException {
    public InvalidPenNameException() {
        super();
    }

    public InvalidPenNameException(Throwable cause) {
        super(cause);
    }

    public InvalidPenNameException(String message) {
        super(message);
    }

    public InvalidPenNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
