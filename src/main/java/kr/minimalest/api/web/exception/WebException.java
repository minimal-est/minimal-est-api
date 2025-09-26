package kr.minimalest.api.web.exception;

public class WebException extends RuntimeException {

    private final OverridableStatusCode overridableStatusCode;

    public WebException() {
        super();
        this.overridableStatusCode = new OverridableStatusCode();
    }

    public WebException(String message) {
        super(message);
        this.overridableStatusCode = new OverridableStatusCode();
    }

    public WebException(String message, Throwable cause) {
        super(message, cause);
        this.overridableStatusCode = new OverridableStatusCode();
    }

    public WebException(Throwable cause) {
        super(cause);
        this.overridableStatusCode = new OverridableStatusCode();
    }

    public WebException(String message, int statusCode) {
        super(message);
        this.overridableStatusCode = new OverridableStatusCode(statusCode);
    }

    public int getOverridableStatusCode() {
        return overridableStatusCode.getStatusCode();
    }
}
