package kr.minimalest.api.application.author;

import java.util.UUID;

public record UpdateAuthorProfileArgument(
        UUID userId,
        String profileImageUrl
) {
    public static UpdateAuthorProfileArgument of(UUID userId, String profileImageUrl) {
        return new UpdateAuthorProfileArgument(userId, profileImageUrl);
    }
}
