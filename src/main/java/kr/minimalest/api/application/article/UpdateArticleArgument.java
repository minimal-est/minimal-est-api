package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.article.ArticleId;
import kr.minimalest.api.domain.article.Content;
import kr.minimalest.api.domain.article.Title;
import org.springframework.util.Assert;

public record UpdateArticleArgument(ArticleId articleId, Title title, Content content) {

    public UpdateArticleArgument {
        Assert.notNull(articleId, "articleId가 null입니다.");
        Assert.notNull(title, "title이 null입니다.");
        Assert.notNull(content, "content가 null입니다.");
    }

    public static UpdateArticleArgument of(ArticleId articleId, Title title, Content content) {
        return new UpdateArticleArgument(articleId, title, content);
    }
}
