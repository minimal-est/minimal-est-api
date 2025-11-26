package kr.minimalest.api.application.blog;

import java.util.UUID;

public record FindProfileImageUrlArgument(
        UUID blogId
) {
}
