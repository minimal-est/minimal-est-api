package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.service.ArticleSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Business
@RequiredArgsConstructor
public class SearchArticles {

    private final ArticleSearchService articleSearchService;
    private final ArticleSummaryCreator articleSummaryCreator;

    @Transactional(readOnly = true)
    public SearchArticlesResult exec(SearchArticlesArgument argument) {
        Pageable pageable = PageRequest.of(
                argument.page(),
                argument.size()
        );

        Page<Article> searchResult = articleSearchService.searchPublishedArticles(
                argument.query(),
                pageable
        );

        List<ArticleSummary> articleSummaries = articleSummaryCreator.create(searchResult.getContent());

        return SearchArticlesResult.of(
                articleSummaries,
                searchResult.getTotalElements(),
                searchResult.getTotalPages(),
                argument.page(),
                argument.size()
        );
    }
}
