package kr.minimalest.api.domain.engagement.recommendation;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.writing.ArticleId;

import java.util.List;

public interface ArticleRecommendationService {

    public List<RecommendedArticle> recommendArticles(int page, int limit);

    public List<RecommendedArticle> recommendArticlesForArticle(ArticleId articleId, int page, int limit);

    public List<RecommendedArticle> recommendArticlesForUser(UserId userId, int page, int limit);
}
