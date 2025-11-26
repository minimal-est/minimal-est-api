package kr.minimalest.api.web.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateCommentRequest(
        @NotBlank(message = "댓글 내용은 비워둘 수 없습니다")
        @Size(max = 5000, message = "댓글은 5000자 이하여야 합니다")
        String content,

        boolean isAnonymous,

        UUID parentCommentId  // null이면 댓글, 값이 있으면 대댓글
) {
}
