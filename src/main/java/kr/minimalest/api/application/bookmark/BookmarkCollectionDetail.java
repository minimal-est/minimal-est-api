package kr.minimalest.api.application.bookmark;

import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollection;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.access.UserId;

import java.time.LocalDateTime;

/**
 * 북마크 컬렉션 상세 정보
 */
public record BookmarkCollectionDetail(
        BookmarkCollectionId id,
        UserId userId,
        String title,
        String description,
        boolean isPublic,
        boolean isDefault,
        int bookmarkCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BookmarkCollectionDetail from(BookmarkCollection collection, int bookmarkCount) {
        return new BookmarkCollectionDetail(
                collection.getId(),
                collection.getUserId(),
                collection.getTitle(),
                collection.getDescription(),
                collection.isPublic(),
                collection.isDefault(),
                bookmarkCount,
                collection.getCreatedAt(),
                collection.getUpdatedAt()
        );
    }
}
