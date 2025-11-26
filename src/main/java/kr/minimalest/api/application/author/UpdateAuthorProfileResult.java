package kr.minimalest.api.application.author;

import kr.minimalest.api.domain.publishing.Author;

import java.util.UUID;

public record UpdateAuthorProfileResult(
        UUID userId,
        String penName,
        String profileImageUrl
) {
    public static UpdateAuthorProfileResult of(Author author) {
        return new UpdateAuthorProfileResult(
                author.getUserId().id(),
                author.getPenName().value(),
                author.getProfile().url()
        );
    }
}
