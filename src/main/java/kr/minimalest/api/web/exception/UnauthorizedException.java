package kr.minimalest.api.web.exception;

public class UnauthorizedException extends WebException {
    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(Throwable cause) {
        super(cause);
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
