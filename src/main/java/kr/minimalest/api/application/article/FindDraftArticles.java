package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class FindDraftArticles {

    private final ArticleRepository articleRepository;
    private final ArticleSummaryCreator articleSummaryCreator;

    @Transactional(readOnly = true)
    public FindDraftArticlesResult exec(FindDraftArticlesArgument argument) {
        Page<Article> articles = articleRepository.findAllDraftedByBlogId(argument.blogId(), argument.pageable());
        Page<ArticleSummary> articleSummaries = articleSummaryCreator.createWithPage(articles);
        return new FindDraftArticlesResult(articleSummaries);
    }
}
