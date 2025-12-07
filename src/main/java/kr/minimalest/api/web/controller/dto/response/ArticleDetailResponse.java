package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.article.ArticleDetail;
import kr.minimalest.api.domain.writing.ArticleStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ArticleDetailResponse(
        UUID articleId,
        String slug,
        String title,
        String content,
        String description,
        ArticleStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime publishedAt,
        AuthorResponse author,
        List<TagResponse> tags
) {
    public static ArticleDetailResponse of(ArticleDetail articleDetail) {
        return new ArticleDetailResponse(
                articleDetail.articleId().id(),
                articleDetail.slug() != null ? articleDetail.slug().value() : null,
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
                ),
                articleDetail.tagNames().stream()
                        .map(tagName -> TagResponse.of(null, tagName))
                        .toList()
        );
    }

}
