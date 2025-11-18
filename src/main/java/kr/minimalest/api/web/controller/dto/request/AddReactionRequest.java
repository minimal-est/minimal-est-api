package kr.minimalest.api.web.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AddReactionRequest(
        @NotNull(message = "반응 타입은 필수입니다.")
        @Schema(description = "반응 타입 (READ, AGREE, USEFUL 중 하나)", example = "AGREE")
        String reactionType
) {
}
