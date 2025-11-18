package kr.minimalest.api.web.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

public record ReactionStatsResponse(
        @Schema(description = "반응 타입별 카운트")
        Map<String, Long> stats
) {
    public static ReactionStatsResponse of(Map<String, Long> stats) {
        return new ReactionStatsResponse(stats);
    }
}
