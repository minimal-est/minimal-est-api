package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.writing.ArticleId;
import org.springframework.util.Assert;

public record PublishArticleArgument(ArticleId articleId) {

    public PublishArticleArgument {
        Assert.notNull(articleId, "articleId가 null입니다.");
    }

    public static PublishArticleArgument of(ArticleId articleId) {
        return new PublishArticleArgument(articleId);
    }
}
