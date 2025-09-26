package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.engagement.recommendation.RecommendedArticle;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.publishing.service.BlogService;
import kr.minimalest.api.domain.engagement.recommendation.ArticleRecommendationService;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FindRecommendArticlesTest {

    @Mock
    ArticleRecommendationService articleRecommendationService;

    @Mock
    BlogService blogService;

    @Mock
    ArticleRepository articleRepository;

    @InjectMocks
    FindRecommendArticles findRecommendArticles;

    @Nested
    @DisplayName("추천 글 조회")
    class happyFindRecommendArticles {

        @Test
        @DisplayName("성공 시 추천 결과 반환")
        void shouldReturnResult() {
            // given
            int page = 0;
            int limit = 3;

            BlogId blogId1 = BlogId.generate();
            Article article1 = Article.create(blogId1);

            BlogId blogId2to3 = BlogId.generate();
            Article article2 = Article.create(blogId2to3);
            Article article3 = Article.create(blogId2to3);

            List<RecommendedArticle> recommendedArticles = List.of(
                    RecommendedArticle.of(article1.getId()),
                    RecommendedArticle.of(article2.getId()),
                    RecommendedArticle.of(article3.getId())
            );

            List<Article> articles = List.of(article1, article2, article3);
            Map<BlogId, PenName> mapPenName = Map.of(
                    blogId1, PenName.of("test1"),
                    blogId2to3, PenName.of("test2to3")
            );

            given(articleRecommendationService.recommendArticles(eq(page), eq(limit))).willReturn(recommendedArticles);
            given(blogService.getMappingPenNames(any(List.class))).willReturn(mapPenName);
            given(articleRepository.findAllByIds(any(List.class))).willReturn(articles);

            FindRecommendArticlesArgument argument = FindRecommendArticlesArgument.limitOf(page, limit);

            // when
            FindRecommendArticlesResult result = findRecommendArticles.exec(argument);

            // then
            Assertions.assertThat(result.articleSummaries()).hasSize(3);
        }
    }
}
