package kr.minimalest.api.web.controller.dto.response;

public record ArticleSummaryOfPrevAndNextResponse(
        ArticleSummaryResponse prevArticleSummary,
        ArticleSummaryResponse nextArticleSummary
) {
}
