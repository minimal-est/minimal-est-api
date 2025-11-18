package kr.minimalest.api.application.reaction;

import java.util.UUID;

/**
 * 아티클 반응 통계 조회 요청
 */
public record GetArticleReactionStatsArgument(
    UUID articleId
) {
    public static GetArticleReactionStatsArgument of(UUID articleId) {
        return new GetArticleReactionStatsArgument(articleId);
    }
}
