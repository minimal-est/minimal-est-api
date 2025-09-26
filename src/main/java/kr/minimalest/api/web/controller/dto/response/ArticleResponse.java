package kr.minimalest.api.web.controller.dto.response;

import java.util.UUID;

public record ArticleResponse(
        UUID articleId,
        UUID authorId,
        String penName,
        String title,
        String content
) {
}
