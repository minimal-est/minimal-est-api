package kr.minimalest.api.application.article;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.engagement.recommendation.ArticleRecommendationService;
import kr.minimalest.api.domain.engagement.recommendation.RecommendedArticle;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
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

        // 추천 서비스에서 정렬된 순서를 보존
        Map<Object, Article> articleMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().id(), article -> article));

        List<Article> sortedArticles = recommendedArticles.stream()
                .map(recommended -> articleMap.get(recommended.articleId().id()))
                .filter(article -> article != null)
                .toList();

        List<ArticleSummary> articleSummaries = articleSummaryCreator.create(sortedArticles);

        return FindRecommendArticlesResult.of(articleSummaries);
    }
}
