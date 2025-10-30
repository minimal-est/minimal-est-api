package kr.minimalest.api.application.blog;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.PenName;

public record FindBlogSelfResult(
        BlogId blogId,
        UserId userId,
        PenName penName
) {
}
