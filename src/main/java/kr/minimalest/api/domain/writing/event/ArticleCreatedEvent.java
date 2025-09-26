package kr.minimalest.api.domain.writing.event;

import kr.minimalest.api.domain.DomainEvent;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.publishing.BlogId;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class ArticleCreatedEvent extends DomainEvent {
    private final ArticleId articleId;
    private final BlogId blogId;

    public ArticleCreatedEvent(ArticleId articleId, BlogId blogId) {
        super();
        Assert.notNull(articleId, "articleId가 null입니다.");
        Assert.notNull(blogId, "blogId가 null입니다.");
        this.articleId = articleId;
        this.blogId = blogId;
    }

    public static ArticleCreatedEvent of(ArticleId articleId, BlogId blogId) {
        return new ArticleCreatedEvent(articleId, blogId);
    }
}
