package kr.minimalest.api.domain.discovery.bookmark.exception;

public class BookmarkAlreadyExistsException extends RuntimeException {
    public BookmarkAlreadyExistsException(String message) {
        super(message);
    }

    public BookmarkAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
