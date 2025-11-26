package kr.minimalest.api.web.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.minimalest.api.application.author.UpdateAuthorProfileResult;

import java.util.UUID;

public record UpdateProfileResponse(
        @Schema(description = "사용자 ID")
        UUID userId,
        @Schema(description = "필명")
        String penName,
        @Schema(description = "프로필 이미지 URL")
        String profileImageUrl
) {
    public static UpdateProfileResponse of(UpdateAuthorProfileResult result) {
        return new UpdateProfileResponse(
                result.userId(),
                result.penName(),
                result.profileImageUrl()
        );
    }
}
