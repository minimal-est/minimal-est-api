package kr.minimalest.api.domain.discovery.bookmark.exception;

public class BookmarkAccessDeniedException extends RuntimeException {
    public BookmarkAccessDeniedException(String message) {
        super(message);
    }

    public BookmarkAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
