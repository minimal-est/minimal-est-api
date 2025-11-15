package kr.minimalest.api.infrastructure.service;

import kr.minimalest.api.domain.engagement.recommendation.RecommendedArticle;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import kr.minimalest.api.domain.engagement.recommendation.ArticleRecommendationService;
import kr.minimalest.api.domain.access.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: 알고리즘 개선하기. 현재는 완료 시각 기준 내림차순 추천
 */

@Service
@RequiredArgsConstructor
public class SimpleArticleRecommendService implements ArticleRecommendationService {

    private final ArticleRepository articleRepository;

    @Override
    public List<RecommendedArticle> recommendArticles(int offset, int limit) {
        List<RecommendedArticle> recommendedArticles = new ArrayList<>();
        List<ArticleId> articleIds = articleRepository.findTopNIdsByOrderByPublishedAtDesc(offset, limit);
        for (ArticleId articleId : articleIds) {
            recommendedArticles.add(RecommendedArticle.of(articleId));
        }
        return recommendedArticles;
    }

    @Override
    public List<RecommendedArticle> recommendArticlesForArticle(ArticleId articleId, int offset, int limit) {
        return List.of();
    }

    @Override
    public List<RecommendedArticle> recommendArticlesForUser(UserId userId, int offset, int limit) {
        return List.of();
    }
}
