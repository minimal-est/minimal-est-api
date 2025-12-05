package kr.minimalest.api.infrastructure.persistence.reaction;

import kr.minimalest.api.domain.engagement.reaction.ArticleReaction;
import kr.minimalest.api.domain.engagement.reaction.ArticleReactionId;
import kr.minimalest.api.domain.engagement.reaction.ReactionState;
import kr.minimalest.api.domain.engagement.reaction.ReactionType;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.access.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA 기반 ArticleReaction 저장소 (Spring Data용)
 * Domain의 ArticleReactionRepository를 구현함
 */
public interface SpringDataJpaArticleReactionRepository extends JpaRepository<ArticleReaction, ArticleReactionId> {

    /**
     * 특정 사용자의 특정 반응 타입 조회
     * PESSIMISTIC_WRITE 락으로 동시성 제어 (Race condition 방지)
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ArticleReaction> findByArticleIdAndUserIdAndReactionType(
            ArticleId articleId,
            UserId userId,
            ReactionType reactionType
    );

    /**
     * 특정 아티클의 모든 활성 반응 (REACTED만)
     */
    List<ArticleReaction> findByArticleIdAndReactionState(
            ArticleId articleId,
            ReactionState reactionState
    );

    /**
     * 특정 아티클의 반응 타입별 활성 카운트 (REACTED만)
     */
    @Query("""
        SELECT new kr.minimalest.api.infrastructure.persistence.reaction.ReactionCountDao(
            ar.articleId, ar.reactionType, COUNT(ar)
        )
        FROM ArticleReaction ar
        WHERE ar.articleId = :articleId
        AND ar.reactionState = 'REACTED'
        GROUP BY ar.reactionType
    """)
    List<ReactionCountDao> countActiveByArticleIdGroupByType(@Param("articleId") ArticleId articleId);

    @Query("""
        SELECT new kr.minimalest.api.infrastructure.persistence.reaction.ReactionCountDao(
            ar.articleId, ar.reactionType, COUNT(ar)
        )
        FROM ArticleReaction ar
        WHERE ar.articleId in :articleIds
        AND ar.reactionState = 'REACTED'
        GROUP BY ar.articleId, ar.reactionType
    """)
    List<ReactionCountDao> countActiveByArticleIdsGroupByType(@Param("articleIds") List<ArticleId> articleIds);

    /**
     * 사용자가 특정 아티클에 한 모든 활성 반응
     */
    List<ArticleReaction> findByArticleIdAndUserIdAndReactionState(
            ArticleId articleId,
            UserId userId,
            ReactionState reactionState
    );
}
