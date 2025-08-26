package kr.minimalest.api.web.controller.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateArticleRequest(
        @NotNull(message = "블로그 ID는 필수입니다")
        @org.hibernate.validator.constraints.UUID
        String blogId
) {
    public UUID blogIdAsUUID() {
        return UUID.fromString(blogId);
    }
}
