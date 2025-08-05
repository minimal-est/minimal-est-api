package kr.minimalest.api.domain.blog;

import kr.minimalest.api.domain.user.UserId;

import java.time.LocalDateTime;

public record Blog(
        BlogId blogId,
        BlogUUID blogUUID,
        UserId userId,
        Title title,
        Description description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static Blog of(
            BlogId blogId,
            BlogUUID blogUUID,
            UserId userId,
            Title title,
            Description description,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new Blog(blogId, blogUUID, userId, title, description, createdAt, updatedAt);
    }

    public static Blog withoutId(
            BlogUUID blogUUID,
            UserId userId,
            Title title,
            Description description,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return Blog.of(null, blogUUID, userId, title, description, createdAt, updatedAt);
    }
}
