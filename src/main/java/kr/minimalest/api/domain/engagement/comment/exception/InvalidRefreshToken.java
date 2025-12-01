package kr.minimalest.api.domain.engagement.comment.exception;

public class InvalidRefreshToken extends RuntimeException {
    public InvalidRefreshToken(String message) {
        super(message);
    }
}
