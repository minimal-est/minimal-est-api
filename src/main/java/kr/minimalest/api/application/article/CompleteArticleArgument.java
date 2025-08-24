package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.article.ArticleId;
import org.springframework.util.Assert;

public record CompleteArticleArgument(ArticleId articleId) {

    public CompleteArticleArgument {
        Assert.notNull(articleId, "articleId가 null입니다.");
    }

    public static CompleteArticleArgument of(ArticleId articleId) {
        return new CompleteArticleArgument(articleId);
    }
}
