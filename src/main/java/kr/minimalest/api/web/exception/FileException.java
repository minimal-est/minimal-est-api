package kr.minimalest.api.web.exception;

public class FileException extends WebException{
    public FileException() {
        super();
    }

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileException(Throwable cause) {
        super(cause);
    }

    public FileException(String message, int statusCode) {
        super(message, statusCode);
    }
}
