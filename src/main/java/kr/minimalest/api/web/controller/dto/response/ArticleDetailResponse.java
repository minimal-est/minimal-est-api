package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.article.ArticleDetail;
import kr.minimalest.api.domain.writing.ArticleStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ArticleDetailResponse(
        UUID articleId,
        String title,
        String content,
        String description,
        ArticleStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime publishedAt,
        AuthorResponse author
) {
    public static ArticleDetailResponse of(ArticleDetail articleDetail) {
        return new ArticleDetailResponse(
                articleDetail.articleId().id(),
                articleDetail.title().value(),
                articleDetail.content().value(),
                articleDetail.description().value(),
                articleDetail.status(),
                articleDetail.createdAt(),
                articleDetail.updatedAt(),
                articleDetail.publishedAt(),
                new AuthorResponse(
                        articleDetail.author().getUserId().id(),
                        articleDetail.author().getPenName().value(),
                        articleDetail.author().getProfile().url()
                )
        );
    }

}
