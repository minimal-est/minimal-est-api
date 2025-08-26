package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.blog.BlogId;
import org.springframework.util.Assert;

public record CreateArticleArgument(BlogId blogId) {

    public CreateArticleArgument {
        Assert.notNull(blogId, "userId는 null일 수 없습니다.");
    }

    public static CreateArticleArgument of(BlogId blogId) {
        return new CreateArticleArgument(blogId);
    }
}
