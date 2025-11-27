package kr.minimalest.api.application.article;

import java.util.UUID;

public record FindPrevAndNextArticleArgument(
        UUID articleId
) {
}
