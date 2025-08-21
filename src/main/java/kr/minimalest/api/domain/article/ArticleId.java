package kr.minimalest.api.domain.article;

import org.springframework.util.Assert;

import java.util.UUID;

public record ArticleId(UUID id) {

    public ArticleId {
        Assert.notNull(id, "id는 null일 수 없습니다.");
    }

    public static ArticleId of(UUID id) {
        return new ArticleId(id);
    }

    public static ArticleId generate() {
        return new ArticleId(UUID.randomUUID());
    }
}
