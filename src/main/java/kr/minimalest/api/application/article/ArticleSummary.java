package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.engagement.reaction.ReactionType;
import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.writing.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Application 계층 내부에서 사용
 * 도메인 필드를 포함하여 비즈니스 로직에 활용
 */
public record ArticleSummary(
        ArticleId articleId,
        Slug slug,
        Title title,
        Description description,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ArticleStatus status,
        AuthorInfo authorInfo,
        ArticleReactionStats articleReactionStats,
        List<String> tagNames
) {
    public static ArticleSummary from(Article article, Author author, Map<ReactionType, Long> reactionStats, List<String> tagNames) {
        return new ArticleSummary(
                article.getId(),
                article.getSlug(),
                article.getTitle(),
                article.getDescription(),
                article.getPublishedAt(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getStatus(),
                new AuthorInfo(
                        author.getId().id(),
                        author.getPenName().value(),
                        author.getProfile().url()
                ),
                new ArticleReactionStats(reactionStats),
                tagNames
        );
    }

    public static ArticleSummary from(Article article, Author author, Map<ReactionType, Long> reactionStats) {
        return from(article, author, reactionStats, List.of());
    }
}
