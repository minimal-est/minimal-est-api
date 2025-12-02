package kr.minimalest.api.application.article;

import java.util.List;

public record SearchArticlesResult(
        List<ArticleSummary> articleSummaries,
        long totalElements,
        int totalPages,
        int currentPage,
        int pageSize
) {

    public static SearchArticlesResult of(
            List<ArticleSummary> articleSummaries,
            long totalElements,
            int totalPages,
            int currentPage,
            int pageSize
    ) {
        return new SearchArticlesResult(articleSummaries, totalElements, totalPages, currentPage, pageSize);
    }
}
