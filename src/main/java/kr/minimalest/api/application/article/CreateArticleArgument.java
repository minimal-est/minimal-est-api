package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.user.UserId;
import org.springframework.util.Assert;

public record CreateArticleArgument(UserId userId) {

    public CreateArticleArgument {
        Assert.notNull(userId, "userId는 null일 수 없습니다.");
    }

    public static CreateArticleArgument of(UserId userId) {
        return new CreateArticleArgument(userId);
    }
}
