package kr.minimalest.api.web.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.minimalest.api.application.article.ArticleSummary;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
public record ArticleSummaryResponse(
        @Schema(description = "아티클 ID")
        UUID articleId,
        @Schema(description = "Slug")
        String slug,
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
        AuthorInfoResponse author,
        @Schema(description = "반응 통계")
        ReactionStatsResponse reactionStats,
        @Schema(description = "태그 목록")
        List<TagResponse> tags
) {
    public static ArticleSummaryResponse of(ArticleSummary articleSummary) {
        return new ArticleSummaryResponse(
                articleSummary.articleId().id(),
                articleSummary.slug() != null ? articleSummary.slug().value() : null,
                articleSummary.title() != null ? articleSummary.title().value() : "",
                articleSummary.description() != null ? articleSummary.description().value() : "",
                articleSummary.publishedAt(),
                articleSummary.createdAt(),
                articleSummary.updatedAt(),
                articleSummary.status().name(),
                AuthorInfoResponse.of(articleSummary.authorInfo()),
                ReactionStatsResponse.ofReactionStats(
                        articleSummary.articleReactionStats().reactionStats()
                ),
                articleSummary.tagNames().stream()
                        .map(name -> TagResponse.of(null, name))
                        .toList()
        );
    }
}
