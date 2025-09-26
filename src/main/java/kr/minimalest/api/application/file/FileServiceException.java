package kr.minimalest.api.application.file;

public class FileServiceException extends RuntimeException {
    public FileServiceException() {
        super();
    }

    public FileServiceException(String message) {
        super(message);
    }

    public FileServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileServiceException(Throwable cause) {
        super(cause);
    }
}
