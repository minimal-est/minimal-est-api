package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.article.ArticleSummary;
import org.springframework.data.domain.Page;

public record ArticleSummaryPageResponse(
        Page<ArticleSummary> articles
) {
    public static ArticleSummaryPageResponse of(Page<ArticleSummary> articles) {
        return new ArticleSummaryPageResponse(articles);
    }
}
