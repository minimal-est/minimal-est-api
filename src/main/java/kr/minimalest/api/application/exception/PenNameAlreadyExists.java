package kr.minimalest.api.application.exception;

public class PenNameAlreadyExists extends BusinessException{
    public PenNameAlreadyExists() {
        super();
    }

    public PenNameAlreadyExists(Throwable cause) {
        super(cause);
    }

    public PenNameAlreadyExists(String message) {
        super(message);
    }

    public PenNameAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }
}
