package kr.minimalest.api.web.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.minimalest.api.application.article.ArticleSummary;
import kr.minimalest.api.application.article.AuthorInfo;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
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
        AuthorInfoResponse author
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
                AuthorInfoResponse.of(articleSummary.authorInfo())
        );
    }

    public record AuthorInfoResponse(
            @Schema(description = "저자 ID")
            UUID authorId,
            @Schema(description = "필명")
            String penName,
            @Schema(description = "프로필 이미지 URL")
            String profileImageUrl
    ) {
        public static AuthorInfoResponse of(AuthorInfo authorInfo) {
            return new AuthorInfoResponse(
                    authorInfo.authorId(),
                    authorInfo.penName(),
                    authorInfo.profileImageUrl()
            );
        }
    }
}
