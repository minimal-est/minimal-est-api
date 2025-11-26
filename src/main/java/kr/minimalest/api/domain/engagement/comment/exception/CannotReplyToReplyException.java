package kr.minimalest.api.domain.engagement.comment.exception;

public class CannotReplyToReplyException extends RuntimeException {
    public CannotReplyToReplyException(String message) {
        super(message);
    }
}
