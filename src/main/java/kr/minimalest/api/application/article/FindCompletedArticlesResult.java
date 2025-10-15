package kr.minimalest.api.application.article;

import org.springframework.data.domain.Page;

public record FindCompletedArticlesResult(
        Page<ArticleSummary> articleSummaries
) {

    public static FindCompletedArticlesResult of(Page<ArticleSummary> articleSummaries) {
        return new FindCompletedArticlesResult(articleSummaries);
    }
}
