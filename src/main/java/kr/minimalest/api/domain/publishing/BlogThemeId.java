package kr.minimalest.api.domain.publishing;

import org.springframework.util.Assert;

import java.util.UUID;

public record BlogThemeId(UUID id) {

    public BlogThemeId {
        Assert.notNull(id, "id는 null일 수 없습니다.");
    }

    public static BlogThemeId of(UUID id) {
        return new BlogThemeId(id);
    }

    public static BlogThemeId generate() {
        return new BlogThemeId(UUID.randomUUID());
    }
}
