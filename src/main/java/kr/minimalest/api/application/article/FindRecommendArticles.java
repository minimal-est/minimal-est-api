package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.engagement.recommendation.ArticleRecommendationService;
import kr.minimalest.api.domain.engagement.recommendation.RecommendedArticle;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Business
@RequiredArgsConstructor
public class FindRecommendArticles {

    private final ArticleRepository articleRepository;
    private final ArticleRecommendationService articleRecommendationService;
    private final ArticleSummaryCreator articleSummaryCreator;

    @Transactional(readOnly = true)
    public FindRecommendArticlesResult exec(FindRecommendArticlesArgument argument) {
        List<RecommendedArticle> recommendedArticles =
                articleRecommendationService.recommendArticles(argument.page(), argument.limit());

        List<Article> articles = articleRepository.findAllByIds(
                recommendedArticles.stream().map(RecommendedArticle::articleId).toList()
        );

        List<ArticleSummary> articleSummaries = articleSummaryCreator.create(articles);

        return FindRecommendArticlesResult.of(articleSummaries);
    }
}
