package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.engagement.recommendation.RecommendedArticle;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.publishing.service.BlogService;
import kr.minimalest.api.domain.engagement.recommendation.ArticleRecommendationService;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Business
@RequiredArgsConstructor
public class FindRecommendArticles {

    private final ArticleRecommendationService articleRecommendationService;
    private final ArticleRepository articleRepository;
    private final BlogService blogService;

    @Transactional(readOnly = true)
    public FindRecommendArticlesResult exec(FindRecommendArticlesArgument argument) {
        List<RecommendedArticle> recommendedArticles =
                articleRecommendationService.recommendArticles(argument.page(), argument.limit());
        List<Article> articles = articleRepository.findAllByIds(
                recommendedArticles.stream().map(RecommendedArticle::articleId).toList()
        );

        List<BlogId> blogIds = articles
                .stream()
                .map(Article::getBlogId)
                .toList();

        Map<BlogId, PenName> mappingPenNames = blogService.getMappingPenNames(blogIds);

        List<ArticleSummary> articleSummaries = articles
                .stream()
                .map(article -> ArticleSummary.from(article, mappingPenNames.get(article.getBlogId())))
                .toList();

        return FindRecommendArticlesResult.of(articleSummaries);
    }
}
