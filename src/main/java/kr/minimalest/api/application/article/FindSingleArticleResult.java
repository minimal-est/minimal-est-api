package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.writing.ArticleStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record FindSingleArticleResult(
        UUID articleId,
        String title,
        String content,
        ArticleStatus status,
        LocalDateTime completedAt
) {

    public static FindSingleArticleResult of(
            UUID articleId,
            String title,
            String content,
            ArticleStatus status,
            LocalDateTime completedAt
    ) {
        return new FindSingleArticleResult(
                articleId,
                title,
                content,
                status,
                completedAt
        );
    }
}
