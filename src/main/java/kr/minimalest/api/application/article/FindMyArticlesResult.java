package kr.minimalest.api.application.article;

import org.springframework.data.domain.Page;

public record FindMyArticlesResult(
        Page<ArticleSummary> articles
) {
}
