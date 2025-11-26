package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.writing.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Application 계층 내부에서 사용
 * 도메인 필드를 포함하여 비즈니스 로직에 활용
 */
public record ArticleSummary(
        ArticleId articleId,
        Title title,
        Description description,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ArticleStatus status,
        Author author
) {
    public static ArticleSummary from(Article article, Author author) {
        return new ArticleSummary(
                article.getId(),
                article.getTitle(),
                article.getDescription(),
                article.getPublishedAt(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getStatus(),
                author
        );
    }
}
