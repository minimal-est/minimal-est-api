package kr.minimalest.api.infrastructure.persistence.reaction;

import kr.minimalest.api.domain.engagement.reaction.ArticleReaction;
import kr.minimalest.api.domain.engagement.reaction.ArticleReactionId;
import kr.minimalest.api.domain.engagement.reaction.ReactionState;
import kr.minimalest.api.domain.engagement.reaction.ReactionType;
import kr.minimalest.api.domain.engagement.reaction.repository.ArticleReactionRepository;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.access.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ArticleReaction Repository 구현
 * Domain 인터페이스를 Spring Data JPA로 구현
 * Redis 연동 시 이 클래스를 대체하면 됨
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class ArticleReactionRepositoryImpl implements ArticleReactionRepository {

    private final SpringDataJpaArticleReactionRepository jpaRepository;

    @Override
    public Optional<ArticleReaction> findByArticleIdAndUserIdAndReactionType(
            ArticleId articleId,
            UserId userId,
            ReactionType reactionType) {
        return jpaRepository.findByArticleIdAndUserIdAndReactionType(articleId, userId, reactionType);
    }

    @Override
    public Map<ReactionType, Long> countActiveByArticleIdGroupByType(ArticleId articleId) {
        List<ReactionCountDao> daos = jpaRepository.countActiveByArticleIdGroupByType(articleId);

        // 모든 ReactionType에 0으로 초기화
        Map<ReactionType, Long> result = new HashMap<>();
        for (ReactionType type : ReactionType.values()) {
            result.put(type, 0L);
        }

        // 실제 데이터로 업데이트
        daos.forEach(dao -> result.put(dao.reactionType(), dao.count()));

        return result;
    }

    @Override
    public Map<ArticleId, Map<ReactionType, Long>> countActiveByArticleIdsGroupByType(List<ArticleId> articleIds) {
        List<ReactionCountDao> daos = jpaRepository.countActiveByArticleIdsGroupByType(articleIds);

        Map<ArticleId, Map<ReactionType, Long>> result = new HashMap<>();

        // 0으로 초기화
        articleIds.forEach(articleId -> {
            Map<ReactionType, Long> stats = new HashMap<>();
            for (ReactionType type : ReactionType.values()) {
                stats.put(type, 0L);
            }
            result.put(articleId, stats);
        });

        // 실제 데이터 업데이트
        daos.forEach(dao -> {
            result.get(dao.articleId()).put(dao.reactionType(), dao.count());
        });

        return result;
    }

    @Override
    public List<ArticleReaction> findActiveByArticleIdAndUserId(
            ArticleId articleId,
            UserId userId) {
        return jpaRepository.findByArticleIdAndUserIdAndReactionState(articleId, userId, ReactionState.REACTED);
    }

    @Override
    public ArticleReaction save(ArticleReaction articleReaction) {
        return jpaRepository.save(articleReaction);
    }

    @Override
    public void delete(ArticleReaction articleReaction) {
        jpaRepository.delete(articleReaction);
    }

    @Override
    public Optional<ArticleReaction> findById(ArticleReactionId id) {
        return jpaRepository.findById(id);
    }
}
