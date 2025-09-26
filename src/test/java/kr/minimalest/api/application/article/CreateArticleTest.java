package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleStatus;
import kr.minimalest.api.domain.writing.event.ArticleCreatedEvent;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.access.UserId;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateArticleTest {

    @InjectMocks
    CreateArticle createArticle;

    @Mock
    ArticleRepository articleRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Getter
    static class ArticleFixture {
        private final UserId userId = UserId.generate();
        private final BlogId blogId = BlogId.generate();
        private final Article article = Article.create(blogId);

        public CreateArticleArgument getCreateArticleArgument() {
            return CreateArticleArgument.of(blogId);
        }
    }

    ArticleFixture fixture = new ArticleFixture();

    @Nested
    @DisplayName("글 생성 시")
    class HappyCreateArticle {

        @Test
        @DisplayName("올바른 상태의 글 ID를 반환하고 글 생성 이벤트가 발행된다")
        public void shouldBeReturnArticleIdAndPublishedEvent() {
            // given
            given(articleRepository.save(any(Article.class))).willReturn(fixture.article.getId());

            // when
            CreateArticleResult result = createArticle.exec(fixture.getCreateArticleArgument());

            // then
            assertThat(result.articleId()).isEqualTo(fixture.article.getId());
            assertThat(fixture.article.getStatus()).isEqualTo(ArticleStatus.DRAFT);
            assertThat(fixture.article.getCompletedAt()).isNull();
            verify(eventPublisher, times(1)).publishEvent(any(ArticleCreatedEvent.class));
        }
    }
}
