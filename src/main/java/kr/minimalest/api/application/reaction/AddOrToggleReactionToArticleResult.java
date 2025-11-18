package kr.minimalest.api.application.reaction;

import java.util.UUID;

/**
 * 반응 추가/토글 응답
 */
public record AddOrToggleReactionToArticleResult(
    UUID reactionId
) {
    public static AddOrToggleReactionToArticleResult of(UUID reactionId) {
        return new AddOrToggleReactionToArticleResult(reactionId);
    }
}
