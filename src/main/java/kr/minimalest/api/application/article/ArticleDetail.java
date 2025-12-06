package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.writing.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Application 계층 내부에서 사용
 * 도메인 필드를 포함하여 비즈니스 로직에 활용
 */
public record ArticleDetail(
        ArticleId articleId,
        Title title,
        Content content,
        Description description,
        ArticleStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime publishedAt,
        Author author,
        List<String> tagNames
) {
    public static ArticleDetail from(Article article, Author author, List<String> tagNames) {
        return new ArticleDetail(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getDescription(),
                article.getStatus(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getPublishedAt(),
                author,
                tagNames
        );
    }

    public static ArticleDetail from(Article article, Author author) {
        return from(article, author, List.of());
    }
}
