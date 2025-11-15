package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleStatus;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class FindMyArticles {

    private final ArticleRepository articleRepository;
    private final ArticleSummaryCreator articleSummaryCreator;

    @Transactional
    public FindMyArticlesResult exec(FindMyArticlesArgument argument) {
        BlogId blogId = BlogId.of(argument.blogId());
        ArticleStatus status = argument.status();
        String searchKeyword = argument.searchKeyword() != null ? argument.searchKeyword() : "";
        Pageable pageable = argument.pageable();

        Page<Article> articles = articleRepository.findAllMyArticles(blogId, status, searchKeyword, pageable);
        Page<ArticleSummary> summaries = articleSummaryCreator.createWithPage(articles);

        return new FindMyArticlesResult(summaries);
    }
}
