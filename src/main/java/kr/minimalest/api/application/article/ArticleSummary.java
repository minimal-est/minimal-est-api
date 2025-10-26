package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.writing.ArticleStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Application 계층에서 Web 계층으로
 * 범용적으로 사용하기 위해 순수 POJO로만 이루어져야 합니다.
 */
public record ArticleSummary(
        UUID articleId,
        String penName,
        String title,
        String content,
        LocalDateTime completedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ArticleStatus status
) {
    public static ArticleSummary of(
            UUID articleId,
            String penName,
            String title,
            String content,
            LocalDateTime completedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            ArticleStatus status
    ) {
        return new ArticleSummary(articleId, penName, title, content, completedAt, createdAt, updatedAt, status);
    }

    public static ArticleSummary from(Article article, PenName penName) {
        return new ArticleSummary(
                article.getRawId(),
                penName.value(),
                article.getTitle().value(),
                article.getContent().value(),
                article.getCompletedAt(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getStatus()
        );
    }
}
