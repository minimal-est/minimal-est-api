package kr.minimalest.api.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.minimalest.api.application.article.*;
import kr.minimalest.api.application.blog.CreateBlog;
import kr.minimalest.api.application.blog.CreateBlogArgument;
import kr.minimalest.api.application.blog.CreateBlogResult;
import kr.minimalest.api.domain.writing.exception.ArticleCompleteFailException;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.web.WithMockJwtUser;
import kr.minimalest.api.web.controller.dto.request.CreateBlogRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(BlogController.class)
class BlogControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CreateBlog createBlog;

    @MockitoBean
    CreateArticle createArticle;

    @MockitoBean
    UpdateArticle updateArticle;

    @MockitoBean
    CompleteArticle completeArticle;

    @Nested
    @DisplayName("블로그 생성 API")
    class CreateBlogAPI {

        @Test
        @DisplayName("정상적인 요청시 블로그를 생성한다")
        @WithMockJwtUser
        void shouldCreateBlogWhenValidRequest() throws Exception {
            // given
            CreateBlogRequest request = CreateBlogRequest.of("testPenName");
            BlogId blogId = BlogId.generate();
            CreateBlogResult result = CreateBlogResult.of(blogId);

            given(createBlog.exec(any(CreateBlogArgument.class))).willReturn(result);

            // when
            ResultActions perform = mockMvc.perform(post("/api/v1/blogs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.blogId").value(blogId.id().toString()));
        }

        @Test
        @DisplayName("펜네임이 올바르지 않는 경우 400 에러를 반환한다")
        @WithMockJwtUser
        void shouldReturn400WhenInvalidPenName() throws Exception {
            // given
            CreateBlogRequest request = CreateBlogRequest.of(".");

            // when
            ResultActions perform = mockMvc.perform(post("/api/v1/blogs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.properties.errors.penName").exists());
        }
    }

    @Nested
    @DisplayName("글 생성 API")
    class CreateArticleAPI {

        @Test
        @DisplayName("정상적인 요청시 200을 반환한다")
        @WithMockJwtUser
        void shouldCreateArticleWhenValidRequest() throws Exception {
            // given
            // TODO
            BlogId blogId = BlogId.generate();
            ArticleId articleId = ArticleId.generate();
            CreateArticleResult result = CreateArticleResult.of(articleId);

            given(createArticle.exec(any(CreateArticleArgument.class))).willReturn(result);

            // when
            ResultActions perform = mockMvc.perform(post("/api/v1/blogs/{blogId}/articles", blogId.id()));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.articleId").value(articleId.id().toString()));
        }
    }

    @Nested
    @DisplayName("글 수정 API")
    class UpdateArticleAPI {

        @Test
        @DisplayName("정상적인 요청시 글 ID와 제목을 반환한다")
        @WithMockJwtUser
        void shouldUpdateArticleWhenValidRequest() throws Exception {
            // given
            BlogId blogId = BlogId.generate();
            ArticleId articleId = ArticleId.generate();
            UpdateArticleArgument argument = UpdateArticleArgument.of(
                    articleId,
                    "제목입니다.",
                    "본문입니다. 테스트입니다."
            );

            // when
            ResultActions perform = mockMvc
                    .perform(put("/api/v1/blogs/{blogId}/articles/{articleId}", blogId.id(), articleId.id())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(argument)));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.articleId").value(articleId.id().toString()))
                    .andExpect(jsonPath("$.title").value(argument.title()));
        }
    }

    @Nested
    @DisplayName("글 완료 API")
    class CompleteArticleAPI {

        private final String COMPLETE_ARTICLE_API_PATH = "/api/v1/blogs/{blogId}/articles/{articleId}/complete";

        @Test
        @DisplayName("정상적인 요청 시 글 ID를 반환한다")
        @WithMockJwtUser
        void shouldCompleteArticleWhenValidRequest() throws Exception {
            // given
            BlogId blogId = BlogId.generate();
            ArticleId articleId = ArticleId.generate();

            // when
            ResultActions perform = mockMvc
                    .perform(post(COMPLETE_ARTICLE_API_PATH,
                            blogId.id(),
                            articleId.id()));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.articleId").value(articleId.id().toString()));
        }

        @Test
        @DisplayName("글 완료 실패 시 400 에러를 반환한다")
        @WithMockJwtUser
        void shouldThrowExceptionWhenTitleLt8() throws Exception {
            // given
            BlogId blogId = BlogId.generate();
            ArticleId articleId = ArticleId.generate();
            willThrow(new ArticleCompleteFailException())
                    .given(completeArticle).exec(any(CompleteArticleArgument.class));

            // when
            ResultActions perform = mockMvc
                    .perform(post(COMPLETE_ARTICLE_API_PATH,
                            blogId.id(),
                            articleId.id()));

            // then
            perform.andExpect(status().isBadRequest());
        }
    }
}
