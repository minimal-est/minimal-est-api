package kr.minimalest.api.application.reaction;

import java.util.UUID;

/**
 * 내 반응 조회 요청
 */
public record GetMyReactionsFromArticleArgument(
    UUID articleId,
    UUID userId
) {
    public static GetMyReactionsFromArticleArgument of(UUID articleId, UUID userId) {
        return new GetMyReactionsFromArticleArgument(articleId, userId);
    }
}
