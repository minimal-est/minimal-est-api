package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.bookmark.BookmarkCollectionDetail;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 북마크 컬렉션 응답 DTO
 */
public record BookmarkCollectionResponse(
        UUID id,
        String title,
        String description,
        boolean isPublic,
        boolean isDefault,
        int bookmarkCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BookmarkCollectionResponse of(BookmarkCollectionDetail detail) {
        return new BookmarkCollectionResponse(
                detail.id().getId(),
                detail.title(),
                detail.description(),
                detail.isPublic(),
                detail.isDefault(),
                detail.bookmarkCount(),
                detail.createdAt(),
                detail.updatedAt()
        );
    }
}
