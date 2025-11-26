package kr.minimalest.api.web.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.minimalest.api.application.article.ArticleSummary;

import java.time.LocalDateTime;
import java.util.UUID;

public record ArticleSummaryResponse(
        @Schema(description = "아티클 ID")
        UUID articleId,
        @Schema(description = "제목")
        String title,
        @Schema(description = "설명")
        String description,
        @Schema(description = "발행 시간")
        LocalDateTime publishedAt,
        @Schema(description = "생성 시간")
        LocalDateTime createdAt,
        @Schema(description = "수정 시간")
        LocalDateTime updatedAt,
        @Schema(description = "상태")
        String status,
        @Schema(description = "저자 정보")
        AuthorDetailResponse author
) {
    public static ArticleSummaryResponse of(ArticleSummary articleSummary) {
        return new ArticleSummaryResponse(
                articleSummary.articleId().id(),
                articleSummary.title() != null ? articleSummary.title().value() : "",
                articleSummary.description() != null ? articleSummary.description().value() : "",
                articleSummary.publishedAt(),
                articleSummary.createdAt(),
                articleSummary.updatedAt(),
                articleSummary.status().name(),
                AuthorDetailResponse.of(articleSummary.author())
        );
    }

    public record AuthorDetailResponse(
            @Schema(description = "저자 ID")
            UUID authorId,
            @Schema(description = "필명")
            String penName,
            @Schema(description = "프로필 이미지 URL")
            String profileImageUrl
    ) {
        public static AuthorDetailResponse of(kr.minimalest.api.domain.publishing.Author author) {
            return new AuthorDetailResponse(
                    author.getUserId().id(),
                    author.getPenName().value(),
                    author.getProfile().url()
            );
        }
    }
}
