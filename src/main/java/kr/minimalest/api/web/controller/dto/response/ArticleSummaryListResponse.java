package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.article.ArticleSummary;

import java.util.List;
import java.util.stream.Collectors;

public record ArticleSummaryListResponse(
        List<ArticleSummaryResponse> articleSummaries
) {
    public static ArticleSummaryListResponse of(List<ArticleSummary> articles) {
        List<ArticleSummaryResponse> responses = articles.stream()
                .map(ArticleSummaryResponse::of)
                .collect(Collectors.toList());
        return new ArticleSummaryListResponse(responses);
    }
}
