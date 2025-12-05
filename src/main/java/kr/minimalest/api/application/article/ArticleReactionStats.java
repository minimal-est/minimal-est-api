package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.engagement.reaction.ReactionType;

import java.util.Map;

public record ArticleReactionStats(
        Map<ReactionType, Long> reactionStats
) {
}
