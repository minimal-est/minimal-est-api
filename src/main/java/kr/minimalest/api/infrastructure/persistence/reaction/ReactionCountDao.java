package kr.minimalest.api.infrastructure.persistence.reaction;

import kr.minimalest.api.domain.engagement.reaction.ReactionType;
import kr.minimalest.api.domain.writing.ArticleId;

/**
 * 반응 카운트 DTO
 * JPA @Query에서 반응 타입별 카운트를 반환할 때 사용
 */
public record ReactionCountDao(
        ArticleId articleId,
        ReactionType reactionType,
        long count
) {
}
