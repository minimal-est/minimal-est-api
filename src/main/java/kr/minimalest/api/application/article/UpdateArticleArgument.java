package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.writing.ArticleId;
import org.springframework.util.Assert;

import java.util.List;

public record UpdateArticleArgument(
        ArticleId articleId, String title, String content, String pureContent, String description, List<String> tags
) {

    public UpdateArticleArgument {
        Assert.notNull(articleId, "articleId가 null입니다.");
    }

    public static UpdateArticleArgument of(ArticleId articleId, String title, String content,
                                           String pureContent, String description, List<String> tags) {
        return new UpdateArticleArgument(articleId, title, content, pureContent, description, tags);
    }
}
