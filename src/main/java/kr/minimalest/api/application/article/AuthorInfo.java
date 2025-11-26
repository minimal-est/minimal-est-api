package kr.minimalest.api.application.article;

import java.util.UUID;

public record AuthorInfo(
        UUID authorId,
        String penName,
        String profileImageUrl
) {
}
