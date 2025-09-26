package kr.minimalest.api.domain.engagement.notification;

import org.springframework.util.Assert;

import java.util.UUID;

public record NotificationId(UUID id) {

    public NotificationId {
        Assert.notNull(id, "id는 null일 수 없습니다.");
    }

    public static NotificationId of(UUID id) {
        return new NotificationId(id);
    }

    public static NotificationId generate() {
        return new NotificationId(UUID.randomUUID());
    }
}
