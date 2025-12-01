package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.writing.ArticleId;
import org.springframework.util.Assert;

public record UpdateArticleArgument(
        ArticleId articleId, String title, String content, String pureContent, String description
) {

    public UpdateArticleArgument {
        Assert.notNull(articleId, "articleId가 null입니다.");
    }

    public static UpdateArticleArgument of(ArticleId articleId, String title, String content,
                                           String pureContent, String description) {
        return new UpdateArticleArgument(articleId, title, content, pureContent, description);
    }
}
