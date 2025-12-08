package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.article.ArticleSummary;
import kr.minimalest.api.application.article.SearchArticlesResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public record ArticleSummaryPageResponse(
        Page<ArticleSummaryResponse> articles
) {
    public static ArticleSummaryPageResponse of(Page<ArticleSummary> articles) {
        Page<ArticleSummaryResponse> responsePage = articles.map(ArticleSummaryResponse::of);
        return new ArticleSummaryPageResponse(responsePage);
    }

    public static ArticleSummaryPageResponse of(SearchArticlesResult searchResult) {
        var responses = searchResult.articleSummaries().stream()
                .map(ArticleSummaryResponse::of)
                .toList();
        Page<ArticleSummaryResponse> responsePage = new PageImpl<>(
                responses,
                PageRequest.of(searchResult.currentPage(), searchResult.pageSize()),
                searchResult.totalElements()
        );
        return new ArticleSummaryPageResponse(responsePage);
    }

    public static ArticleSummaryPageResponse of(
            List<ArticleSummary> articles,
            long totalElements,
            int currentPage,
            int pageSize
    ) {
        var responses = articles.stream()
                .map(ArticleSummaryResponse::of)
                .toList();
        Page<ArticleSummaryResponse> responsePage = new PageImpl<>(
                responses,
                PageRequest.of(currentPage, pageSize),
                totalElements
        );
        return new ArticleSummaryPageResponse(responsePage);
    }
}
