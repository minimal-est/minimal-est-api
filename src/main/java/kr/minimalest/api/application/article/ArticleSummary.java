package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.writing.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Application 계층에서 Web 계층으로
 * 범용적으로 사용하기 위해 순수 POJO로만 이루어져야 합니다.
 */
public record ArticleSummary(
        UUID articleId,
        String title,
        String description,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ArticleStatus status,
        AuthorInfo author
) {
    public static ArticleSummary of(
            UUID articleId,
            String title,
            String description,
            LocalDateTime publishedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            ArticleStatus status,
            UUID authorId,
            String penName
    ) {
        return new ArticleSummary(
                articleId,
                title,
                description,
                publishedAt,
                createdAt,
                updatedAt,
                status,
                new AuthorInfo(
                        authorId,
                        penName
                )
        );
    }

    public static ArticleSummary from(Article article, Author author) {
        String title = Optional.ofNullable(article.getTitle())
                .map(Title::value)
                .orElse("");

        String description = Optional.ofNullable(article.getDescription())
                .map(Description::value)
                .orElse("");

        return new ArticleSummary(
                article.getRawId(),
                title,
                description,
                article.getPublishedAt(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getStatus(),
                new AuthorInfo(
                        author.getId().id(),
                        author.getPenName().value()
                )
        );
    }
}
