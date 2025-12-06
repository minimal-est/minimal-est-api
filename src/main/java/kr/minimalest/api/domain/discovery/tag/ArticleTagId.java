package kr.minimalest.api.domain.discovery.tag;

import org.springframework.util.Assert;

import java.util.UUID;

public record ArticleTagId(UUID id) {

    public ArticleTagId {
        Assert.notNull(id, "id는 null일 수 없습니다.");
    }

    public static ArticleTagId of(UUID id) {
        return new ArticleTagId(id);
    }

    public static ArticleTagId generate() {
        return new ArticleTagId(UUID.randomUUID());
    }
}
