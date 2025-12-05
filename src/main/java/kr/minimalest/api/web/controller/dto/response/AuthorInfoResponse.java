package kr.minimalest.api.web.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.minimalest.api.application.article.AuthorInfo;

import java.util.UUID;

public record AuthorInfoResponse(
        @Schema(description = "저자 ID")
        UUID authorId,
        @Schema(description = "필명")
        String penName,
        @Schema(description = "프로필 이미지 URL")
        String profileImageUrl
) {
    public static AuthorInfoResponse of(AuthorInfo authorInfo) {
        return new AuthorInfoResponse(
                authorInfo.authorId(),
                authorInfo.penName(),
                authorInfo.profileImageUrl()
        );
    }
}
