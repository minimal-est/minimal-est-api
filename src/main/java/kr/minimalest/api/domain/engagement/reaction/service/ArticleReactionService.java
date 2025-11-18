package kr.minimalest.api.domain.engagement.reaction.service;

import kr.minimalest.api.domain.engagement.reaction.ArticleReactionId;
import kr.minimalest.api.domain.engagement.reaction.ReactionType;
import kr.minimalest.api.domain.writing.ArticleId;

import java.util.Map;

public interface ArticleReactionService {

    /**
     * 실제 ArticleReaction을 저장(영속)합니다
     * @param articleReactionId 대상 ID
     */
    void react(ArticleReactionId articleReactionId);

    void removeReaction(ArticleReactionId articleReactionId);

    Map<ReactionType, Long> getReactionCounts(ArticleId articleId);
}
