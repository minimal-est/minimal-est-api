package kr.minimalest.api.domain.access.exception;

public class EmailSendForVerificationFailedException extends RuntimeException{
    public EmailSendForVerificationFailedException() {
        super();
    }

    public EmailSendForVerificationFailedException(Throwable cause) {
        super(cause);
    }

    public EmailSendForVerificationFailedException(String message) {
        super(message);
    }

    public EmailSendForVerificationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
