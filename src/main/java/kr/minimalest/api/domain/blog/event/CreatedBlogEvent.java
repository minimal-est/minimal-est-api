package kr.minimalest.api.domain.blog.event;

import kr.minimalest.api.domain.DomainEvent;
import kr.minimalest.api.domain.blog.BlogId;
import kr.minimalest.api.domain.blog.PenName;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class CreatedBlogEvent extends DomainEvent {

    private final BlogId blogId;
    private final PenName penName;

    private CreatedBlogEvent(BlogId blogId, PenName penName) {
        super();
        Assert.notNull(blogId, "BlogId는 null일 수 없습니다.");
        Assert.notNull(penName, "PenName은 null일 수 없습니다.");
        this.penName = penName;
        this.blogId = blogId;
    }

    public static CreatedBlogEvent of(BlogId blogId, PenName penName) {
        return new CreatedBlogEvent(blogId, penName);
    }
}
