package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.publishing.service.BlogService;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Business
@RequiredArgsConstructor
public class FindDraftArticles {

    private final ArticleRepository articleRepository;
    private final BlogService blogService;

    public FindRecommendArticlesResult exec(FindDraftArticlesArgument argument) {
        List<Article> articles = articleRepository.findAllDraftedByBlogId(argument.blogId(), argument.pageable());
        List<BlogId> blogIds = articles.stream().map(Article::getBlogId).toList();
        Map<BlogId, PenName> mappingPenNames = blogService.getMappingPenNames(blogIds);
        List<ArticleSummary> articleSummaries = articles.stream()
                .map(article -> ArticleSummary.from(article, mappingPenNames.get(article.getBlogId())))
                .toList();
        return new FindRecommendArticlesResult(articleSummaries);
    }
}
