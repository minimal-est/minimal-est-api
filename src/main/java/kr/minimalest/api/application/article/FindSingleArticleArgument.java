package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.writing.ArticleId;
import org.springframework.util.Assert;

public record FindSingleArticleArgument(ArticleId articleId) {

    public FindSingleArticleArgument {
        Assert.notNull(articleId, "articleId는 null일 수 없습니다.");
    }

    public static FindSingleArticleArgument of(ArticleId articleId) {
        return new FindSingleArticleArgument(articleId);
    }
}
