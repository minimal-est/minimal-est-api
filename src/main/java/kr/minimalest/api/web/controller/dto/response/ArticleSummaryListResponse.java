package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.article.ArticleSummary;

import java.util.List;

public record ArticleSummaryListResponse(
        List<ArticleSummary> articleSummaries
) {
}
