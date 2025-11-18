package kr.minimalest.api.application.reaction;

import kr.minimalest.api.domain.engagement.reaction.ArticleReaction;

import java.time.LocalDateTime;
import java.util.UUID;

public record MyReactionDetail(
        UUID reactionId,
        String reactionType,
        String reactionState,
        LocalDateTime reactAt
) {
    public static MyReactionDetail of(ArticleReaction reaction) {
        return new MyReactionDetail(
                reaction.getId().id(),
                reaction.getReactionType().name(),
                reaction.getReactionState().name(),
                reaction.getReactedAt()
        );
    }
}
