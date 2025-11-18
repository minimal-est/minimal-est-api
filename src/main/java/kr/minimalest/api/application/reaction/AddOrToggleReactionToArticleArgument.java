package kr.minimalest.api.application.reaction;

import java.util.UUID;

/**
 * 반응 추가/토글 요청
 */
public record AddOrToggleReactionToArticleArgument(
    UUID articleId,
    UUID userId,
    String reactionType
) {
    public static AddOrToggleReactionToArticleArgument of(UUID articleId, UUID userId, String reactionType) {
        return new AddOrToggleReactionToArticleArgument(articleId, userId, reactionType);
    }
}
