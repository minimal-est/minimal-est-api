package kr.minimalest.api.application.blog;

import kr.minimalest.api.domain.publishing.BlogId;
import org.springframework.util.Assert;

public record CreateBlogResult(BlogId blogId) {

    public CreateBlogResult {
        Assert.notNull(blogId, "blogId는 null일 수 없습니다.");
    }

    public static CreateBlogResult of(BlogId blogId) {
        return new CreateBlogResult(blogId);
    }
}
