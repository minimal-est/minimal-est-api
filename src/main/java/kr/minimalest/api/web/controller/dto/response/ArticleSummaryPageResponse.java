package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.article.ArticleSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public record ArticleSummaryPageResponse(
        Page<ArticleSummaryResponse> articles
) {
    public static ArticleSummaryPageResponse of(Page<ArticleSummary> articles) {
        Page<ArticleSummaryResponse> responsePage = articles.map(ArticleSummaryResponse::of);
        return new ArticleSummaryPageResponse(responsePage);
    }
}
