package kr.minimalest.api.domain.engagement.comment;

import jakarta.persistence.Embeddable;
import org.springframework.util.Assert;

@Embeddable
public record CommentContent(String value) {
    public CommentContent {
        Assert.hasText(value, "댓글 내용은 비워둘 수 없습니다");
        Assert.isTrue(value.length() <= 5000, "댓글은 5000자 이하여야 합니다");
    }
}
