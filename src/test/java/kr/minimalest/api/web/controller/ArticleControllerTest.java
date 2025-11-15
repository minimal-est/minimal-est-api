package kr.minimalest.api.web.controller;

import kr.minimalest.api.application.article.*;
import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.publishing.*;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.ArticleStatus;
import kr.minimalest.api.domain.writing.Content;
import kr.minimalest.api.domain.writing.Description;
import kr.minimalest.api.domain.writing.Title;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(ArticleController.class)
class ArticleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    FindSingleArticle findSingleArticle;

    @MockitoBean
    FindRecommendArticles findRecommendArticles;

    @Nested
    @DisplayName("글 조회 API")
    class findArticle {

        @Test
        @DisplayName("글 단건 조회 성공")
        void shouldReturnSingleArticle() throws Exception {
            // given
            ArticleId articleId = ArticleId.generate();
            Blog blog = Blog.create(UserId.generate(), PenName.of("test-pen-name"));
            ArticleDetail articleDetail = new ArticleDetail(
                    articleId.id(),
                    "제목입니다.",
                    "내용입니다.",
                    "설명입니다.",
                    ArticleStatus.PUBLISHED,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    new AuthorInfo(blog.getAuthor().getId().id(), blog.getAuthor().getPenName().value())
            );
            FindSingleArticleResult result = new FindSingleArticleResult(articleDetail);

            given(findSingleArticle.exec(any(FindSingleArticleArgument.class))).willReturn(result);

            // when
            ResultActions perform = mockMvc.perform(get("/api/v1/articles/{articleId}", articleId.id()));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.articleId").value(articleId.id().toString()));
        }

        @Test
        @DisplayName("추천 글 여러 개 조회 성공")
        void shouldReturnRecommendArticles() throws Exception {
            // given
            int size = 5;
            Blog blog = Blog.create(UserId.generate(), PenName.of("test-pen-name"));
            List<Article> articles = new ArrayList<>();
            for (int i = 0; i < size * 2; i++) {
                Article article = Article.create(BlogId.generate());
                articles.add(article);
            }

            List<ArticleSummary> articleSummaries = new ArrayList<>();
            for (Article article : articles) {
                articleSummaries.add(ArticleSummary.from(article, blog.getAuthor()));
            }

            FindRecommendArticlesResult result = FindRecommendArticlesResult.of(articleSummaries);
            given(findRecommendArticles.exec(any(FindRecommendArticlesArgument.class)))
                    .willAnswer(invocation -> {
                        FindRecommendArticlesArgument arg = invocation.getArgument(0);
                        List<ArticleSummary> limited = articleSummaries.stream()
                                .limit(arg.limit())
                                .toList();
                        return FindRecommendArticlesResult.of(limited);
                    });

            // when
            ResultActions perform = mockMvc.perform(get("/api/v1/articles/recommend")
                    .param("size", String.valueOf(size)));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.articleSummaries.length()").value(5));
        }
    }
}
