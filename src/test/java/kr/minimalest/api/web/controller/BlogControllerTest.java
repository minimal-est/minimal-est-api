package kr.minimalest.api.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.minimalest.api.application.article.*;
import kr.minimalest.api.application.blog.*;
import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.PenName;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(BlogController.class)
class BlogControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    FindBlog findBlog;

    @MockitoBean
    FindBlogSelf findBlogSelf;

    @MockitoBean
    CreateBlog createBlog;

    @MockitoBean
    CreateArticle createArticle;

    @MockitoBean
    UpdateArticle updateArticle;

    @MockitoBean
    PublishArticle completeArticle;

    @MockitoBean
    FindDraftArticles findDraftArticles;

    @MockitoBean
    FindCompletedArticles findCompletedArticles;

    @Nested
    @DisplayName("블로그 찾기 API")
    class FindBlogSelfAPI {

        @Test
        @DisplayName("사용자 ID로 블로그 정보를 찾는다")
        @WithMockJwtUser
        void shouldFindBlogSelfWhenValidRequest() throws Exception {
            // given
            UUID mockUserUUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
            UserId mockUserId = UserId.of(mockUserUUID);
            PenName penName = PenName.of("test-1234");
            Blog mockBlog = Blog.create(mockUserId, penName);
            FindBlogSelfArgument argument = new FindBlogSelfArgument(mockUserId);
            FindBlogSelfResult result = new FindBlogSelfResult(mockBlog.getId(), mockUserId, penName);
            given(findBlogSelf.exec(argument)).willReturn(result);

            // when
            ResultActions perform = mockMvc.perform(get("/api/v1/blogs/self"));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.blogId").value(mockBlog.getId().id().toString()))
                    .andExpect(jsonPath("$.userId").value(mockUserId.id().toString()))
                    .andExpect(jsonPath("$.penName").value(penName.value()))
                    .andDo(print());

            verify(findBlogSelf).exec(argument);
        }

        @Test
        @DisplayName("블로그 펜네임으로 블로그 정보를 찾는다")
        void shouldFindBlogWhenValidRequest() throws Exception {
            // given
            PenName penName = PenName.of("test");
            Blog mockBlog = Blog.create(UserId.generate(), penName);
            FindBlogArgument argument = new FindBlogArgument(penName.value());
            FindBlogResult result = new FindBlogResult(mockBlog.getId(), mockBlog.getPenName());

            given(findBlog.exec(argument)).willReturn(result);

            // when
            ResultActions perform = mockMvc.perform(get("/api/v1/blogs/{penName}", penName.value()));

            // then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.blogId").value(mockBlog.getId().id().toString()))
                    .andExpect(jsonPath("$.penName").value(mockBlog.getPenName().value()))
                    .andDo(print());
        }
    }

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
                    "본문입니다. 테스트입니다.",
                    "설명입니다."
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
    class PublishArticleAPI {

        private final String COMPLETE_ARTICLE_API_PATH = "/api/v1/blogs/{blogId}/articles/{articleId}/complete";

        @Test
        @DisplayName("정상적인 요청 시 글 ID를 반환한다")
        @WithMockJwtUser
        void shouldPublishArticleWhenValidRequest() throws Exception {
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
                    .given(completeArticle).exec(any(PublishArticleArgument.class));

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
