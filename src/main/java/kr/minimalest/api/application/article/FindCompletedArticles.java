package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@Business
@RequiredArgsConstructor
public class FindCompletedArticles {

    private final ArticleRepository articleRepository;
    private final ArticleSummaryCreator articleSummaryCreator;

    public FindCompletedArticlesResult exec(FindCompletedArticlesArgument argument) {
        Page<Article> articles = articleRepository.findAllCompletedByBlogId(argument.blogId(), argument.pageable());
        Page<ArticleSummary> articleSummaries = articleSummaryCreator.createWithPage(articles);
        return new FindCompletedArticlesResult(articleSummaries);
    }
}
