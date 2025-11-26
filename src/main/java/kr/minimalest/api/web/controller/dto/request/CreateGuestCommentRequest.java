package kr.minimalest.api.web.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateGuestCommentRequest(
        @NotBlank(message = "이름은 비워둘 수 없습니다")
        @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하여야 합니다")
        String authorName,

        @NotBlank(message = "댓글 내용은 비워둘 수 없습니다")
        @Size(max = 5000, message = "댓글은 5000자 이하여야 합니다")
        String content,

        @NotBlank(message = "비밀번호는 비워둘 수 없습니다")
        @Size(min = 2, max = 20, message = "비밀번호는 2자 이상 20자 이하여야 합니다")
        String password,

        UUID parentCommentId,  // null이면 댓글, 값이 있으면 대댓글

        // TODO: reCAPTCHA 토큰 (나중에 추가)
        String recaptchaToken
) {
}
