package kr.minimalest.api.application.reaction;

import kr.minimalest.api.domain.engagement.reaction.repository.ArticleReactionRepository;
import kr.minimalest.api.domain.writing.ArticleId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 아티클 반응 통계 조회 Use Case
 * 특정 아티클의 반응 타입별 활성 카운트를 조회합니다.
 */
@Component
@RequiredArgsConstructor
public class GetArticleReactionStats {

    private final ArticleReactionRepository reactionRepository;

    @Transactional(readOnly = true)
    public GetArticleReactionStatsResult exec(GetArticleReactionStatsArgument argument) {
        var stats = reactionRepository.countActiveByArticleIdGroupByType(ArticleId.of(argument.articleId()));
        return GetArticleReactionStatsResult.of(stats);
    }
}
