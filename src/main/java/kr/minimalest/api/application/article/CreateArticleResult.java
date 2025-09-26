package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.writing.ArticleId;
import org.springframework.util.Assert;

public record CreateArticleResult(ArticleId articleId) {

    public CreateArticleResult {
        Assert.notNull(articleId, "articleId는 null일 수 없습니다.");
    }

    public static CreateArticleResult of(ArticleId articleId) {
        return new CreateArticleResult(articleId);
    }
}
