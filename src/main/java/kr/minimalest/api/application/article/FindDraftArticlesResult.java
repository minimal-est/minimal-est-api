package kr.minimalest.api.application.article;

import org.springframework.data.domain.Page;

public record FindDraftArticlesResult(
        Page<ArticleSummary> articleSummaries
) {
}
