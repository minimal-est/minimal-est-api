package kr.minimalest.api.domain.user;

import org.springframework.util.Assert;

import java.util.UUID;

public record RoleId(UUID id) {

    public RoleId {
        Assert.notNull(id, "id는 null일 수 없습니다.");
    }

    public RoleId() {
        this(UUID.randomUUID());
    }

    public static RoleId generate() {
        return new RoleId(UUID.randomUUID());
    }
}
