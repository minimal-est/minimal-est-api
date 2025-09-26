package kr.minimalest.api.application.article;

import java.util.List;

public record FindRecommendArticlesResult(
        List<ArticleSummary> articleSummaries
) {

    public static FindRecommendArticlesResult of(List<ArticleSummary> articleSummaries) {
        return new FindRecommendArticlesResult(articleSummaries);
    }
}
