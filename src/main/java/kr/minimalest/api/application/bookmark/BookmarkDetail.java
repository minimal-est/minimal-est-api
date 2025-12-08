package kr.minimalest.api.application.bookmark;

import kr.minimalest.api.domain.discovery.bookmark.Bookmark;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkId;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;

import java.time.LocalDateTime;

/**
 * 북마크 상세 정보
 */
public record BookmarkDetail(
        BookmarkId id,
        BookmarkCollectionId collectionId,
        ArticleId articleId,
        String articleSlug,
        String articleTitle,
        String authorPenName,
        int sequence,
        LocalDateTime createdAt
) {
    public static BookmarkDetail from(Bookmark bookmark, Article article, Blog blog) {
        return new BookmarkDetail(
                bookmark.getId(),
                bookmark.getCollectionId(),
                bookmark.getArticleId(),
                article.getSlug().value(),
                article.getTitle().value(),
                blog.getPenName().value(),
                bookmark.getSequence(),
                bookmark.getCreatedAt()
        );
    }
}
