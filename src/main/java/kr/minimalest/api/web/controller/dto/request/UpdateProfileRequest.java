package kr.minimalest.api.web.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
        @NotBlank(message = "프로필 이미지 URL은 필수입니다.")
        @Schema(description = "S3 업로드 후 얻은 프로필 이미지 URL", example = "https://s3.amazonaws.com/...")
        String profileImageUrl
) {
}
