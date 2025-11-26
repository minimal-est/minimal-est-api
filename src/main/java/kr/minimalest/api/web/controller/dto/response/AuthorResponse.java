package kr.minimalest.api.web.controller.dto.response;

import java.util.UUID;

public record AuthorResponse(
        UUID authorId,
        String penName,
        String profileImageUrl
) {
}
