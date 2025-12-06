package kr.minimalest.api.domain.discovery.tag;

import org.springframework.util.Assert;

import java.util.UUID;

public record TagId(UUID id) {

    public TagId {
        Assert.notNull(id, "id는 null일 수 없습니다.");
    }

    public static TagId of(UUID id) {
        return new TagId(id);
    }

    public static TagId generate() {
        return new TagId(UUID.randomUUID());
    }
}
