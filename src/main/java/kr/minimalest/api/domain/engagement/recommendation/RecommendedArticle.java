package kr.minimalest.api.domain.engagement.recommendation;

import kr.minimalest.api.domain.writing.ArticleId;
import org.springframework.util.Assert;

public record RecommendedArticle(ArticleId articleId) {

    public RecommendedArticle {
        Assert.notNull(articleId, "articleId는 null일 수 없습니다.");
    }

    public static RecommendedArticle of(ArticleId articleId) {
        return new RecommendedArticle(articleId);
    }
}
