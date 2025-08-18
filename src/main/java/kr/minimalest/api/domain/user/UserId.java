package kr.minimalest.api.domain.user;

import org.springframework.util.Assert;

import java.util.UUID;

public record UserId(UUID id) {

    public UserId {
        Assert.notNull(id, "id는 null일 수 없습니다.");
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId of(UUID id) {
        return new UserId(id);
    }
}
