package kr.minimalest.api.application.blog;

import kr.minimalest.api.domain.user.UserId;
import org.springframework.util.Assert;

public record CreateBlogArgument(UserId userId, String penName) {

    public CreateBlogArgument {
        Assert.notNull(userId, "userId는 필수입니다.");
        Assert.hasText(penName, "penName은 값을 가져야합니다.");
    }

    public static CreateBlogArgument of(UserId userId, String penName) {
        return new CreateBlogArgument(userId, penName);
    }
}
