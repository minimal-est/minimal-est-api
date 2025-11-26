package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.bookmark.BookmarkDetail;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 북마크 응답 DTO
 */
public record BookmarkResponse(
        UUID id,
        UUID collectionId,
        UUID articleId,
        String articleTitle,
        String authorPenName,
        int sequence,
        LocalDateTime createdAt
) {
    public static BookmarkResponse of(BookmarkDetail detail) {
        return new BookmarkResponse(
                detail.id().getId(),
                detail.collectionId().getId(),
                detail.articleId().id(),
                detail.articleTitle(),
                detail.authorPenName(),
                detail.sequence(),
                detail.createdAt()
        );
    }
}
