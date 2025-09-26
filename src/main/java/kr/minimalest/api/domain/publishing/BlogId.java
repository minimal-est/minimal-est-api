package kr.minimalest.api.domain.publishing;

import org.springframework.util.Assert;

import java.util.UUID;

public record BlogId(UUID id) {

    public BlogId {
        Assert.notNull(id, "id는 null일 수 없습니다.");
    }

    public static BlogId of(UUID id) {
        return new BlogId(id);
    }

    public static BlogId generate() {
        return new BlogId(UUID.randomUUID());
    }
}
