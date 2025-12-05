package kr.minimalest.api.domain.engagement.reaction.repository;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.engagement.reaction.ArticleReaction;
import kr.minimalest.api.domain.engagement.reaction.ReactionType;
import kr.minimalest.api.domain.writing.ArticleId;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ArticleReaction 저장소 인터페이스
 */
public interface ArticleReactionRepository {

    /**
     * 특정 사용자의 특정 반응 타입 조회
     * 사용자가 이미 이 반응을 했는지 확인할 때 사용
     * @param articleId 아티클 ID
     * @param userId 사용자 ID
     * @param reactionType 반응 타입 (READ, AGREE, USEFUL)
     * @return 해당 반응 (있으면), 없으면 empty
     */
    Optional<ArticleReaction> findByArticleIdAndUserIdAndReactionType(
            ArticleId articleId,
            UserId userId,
            ReactionType reactionType
    );

    /**
     * 특정 아티클의 반응 타입별 활성 카운트 조회 (REACTED만 포함)
     * @param articleId 아티클 ID
     * @return ReactionType별 카운트 (READ: 10, AGREE: 5, USEFUL: 3)
     */
    Map<ReactionType, Long> countActiveByArticleIdGroupByType(ArticleId articleId);

    Map<ArticleId, Map<ReactionType, Long>> countActiveByArticleIdsGroupByType(List<ArticleId> articleIds);

    /**
     * 사용자가 특정 아티클에 한 모든 활성 반응 조회
     * @param articleId 아티클 ID
     * @param userId 사용자 ID
     * @return 활성 반응 리스트 (REACTED 상태인 것만)
     */
    List<ArticleReaction> findActiveByArticleIdAndUserId(ArticleId articleId, UserId userId);

    /**
     * 반응 저장 (생성 또는 업데이트)
     * @param articleReaction 저장할 반응
     * @return 저장된 반응
     */
    ArticleReaction save(ArticleReaction articleReaction);

    /**
     * 반응 삭제
     * @param articleReaction 삭제할 반응
     */
    void delete(ArticleReaction articleReaction);

    /**
     * ID로 반응 조회
     * @param id 반응 ID
     * @return 반응 (있으면), 없으면 empty
     */
    Optional<ArticleReaction> findById(kr.minimalest.api.domain.engagement.reaction.ArticleReactionId id);
}
