package kr.minimalest.api.application.blog;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.BlogId;

public record BlogInfo(
        BlogId blogId,
        UserId userId,
        Author author
) {
    public static BlogInfo of(Blog blog) {
        return new BlogInfo(
                blog.getId(),
                blog.getOwnerUserId(),
                blog.getAuthor()
        );
    }
}
