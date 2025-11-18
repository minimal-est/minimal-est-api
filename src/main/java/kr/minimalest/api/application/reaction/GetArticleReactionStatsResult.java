package kr.minimalest.api.application.reaction;

import kr.minimalest.api.domain.engagement.reaction.ReactionType;

import java.util.HashMap;
import java.util.Map;

/**
 * 아티클 반응 통계 조회 응답
 */
public record GetArticleReactionStatsResult(
    Map<String, Long> stats
) {
    public static GetArticleReactionStatsResult of(Map<ReactionType, Long> reactionStats) {
        // 모든 ReactionType을 0으로 초기화
        Map<String, Long> stringKeyMap = new HashMap<>();
        for (ReactionType type : ReactionType.values()) {
            stringKeyMap.put(type.name(), 0L);
        }

        // 실제 데이터로 업데이트
        reactionStats.forEach((type, count) -> stringKeyMap.put(type.name(), count));

        return new GetArticleReactionStatsResult(stringKeyMap);
    }
}
