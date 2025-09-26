package kr.minimalest.api.domain.engagement.feedback;

import org.springframework.util.Assert;

import java.util.UUID;

public record ArticleReactionId(UUID id) {

    public ArticleReactionId {
        Assert.notNull(id, "id는 null일 수 없습니다.");
    }

    public static ArticleReactionId of(UUID id) {
        return new ArticleReactionId(id);
    }

    public static ArticleReactionId generate() {
        return new ArticleReactionId(UUID.randomUUID());
    }
}
