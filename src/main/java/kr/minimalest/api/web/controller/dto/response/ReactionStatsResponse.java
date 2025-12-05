package kr.minimalest.api.web.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.minimalest.api.domain.engagement.reaction.ReactionType;

import java.util.Map;
import java.util.stream.Collectors;

public record ReactionStatsResponse(
        @Schema(description = "반응 타입별 카운트")
        Map<String, Long> stats
) {
    public static ReactionStatsResponse of(Map<String, Long> stats) {
        return new ReactionStatsResponse(stats);
    }

    public static ReactionStatsResponse ofReactionStats(Map<ReactionType, Long> stats) {
        var stringStats = stats.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().name(),
                        Map.Entry::getValue
                ));
        return new ReactionStatsResponse(stringStats);
    }
}
