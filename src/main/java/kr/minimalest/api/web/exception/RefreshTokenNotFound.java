package kr.minimalest.api.web.exception;

public class RefreshTokenNotFound extends WebException {
    public RefreshTokenNotFound(String message, int statusCode) {
        super(message, statusCode);
    }

    public RefreshTokenNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public RefreshTokenNotFound(String message) {
        super(message);
    }

    public RefreshTokenNotFound(Throwable cause) {
        super(cause);
    }

    public RefreshTokenNotFound() {
        super();
    }

    @Override
    public int getOverridableStatusCode() {
        return super.getOverridableStatusCode();
    }
}
