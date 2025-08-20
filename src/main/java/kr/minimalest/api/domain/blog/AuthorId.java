package kr.minimalest.api.domain.blog;

import org.springframework.util.Assert;

import java.util.UUID;

public record AuthorId(UUID id) {

    public AuthorId {
        Assert.notNull(id, "id는 null일 수 없습니다.");
    }

    public static AuthorId of(UUID id) {
        return new AuthorId(id);
    }

    public static AuthorId generate() {
        return new AuthorId(UUID.randomUUID());
    }
}
