package kr.minimalest.api.domain.discovery.bookmark.exception;

public class BookmarkNotFoundException extends RuntimeException {
    public BookmarkNotFoundException(String message) {
        super(message);
    }

    public BookmarkNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
