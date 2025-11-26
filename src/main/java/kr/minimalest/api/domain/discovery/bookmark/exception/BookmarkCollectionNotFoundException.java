package kr.minimalest.api.domain.discovery.bookmark.exception;

public class BookmarkCollectionNotFoundException extends RuntimeException {
    public BookmarkCollectionNotFoundException(String message) {
        super(message);
    }

    public BookmarkCollectionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
