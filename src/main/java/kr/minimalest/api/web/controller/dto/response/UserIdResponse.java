package kr.minimalest.api.web.controller.dto.response;

import java.util.UUID;

public record UserIdResponse(
        UUID userId
) {
    public static UserIdResponse of(UUID userId) {
        return new UserIdResponse(userId);
    }
}
