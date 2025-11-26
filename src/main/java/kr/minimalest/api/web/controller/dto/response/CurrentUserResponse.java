package kr.minimalest.api.web.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CurrentUserResponse(
        @Schema(description = "사용자 ID")
        UUID userId
) {
    public static CurrentUserResponse of(UUID userId) {
        return new CurrentUserResponse(userId);
    }
}
