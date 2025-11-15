package kr.minimalest.api.application.article;

import kr.minimalest.api.domain.writing.exception.ArticleCompleteFailException;
import kr.minimalest.api.domain.writing.exception.ArticleNotFoundException;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleStatus;
import kr.minimalest.api.domain.writing.Content;
import kr.minimalest.api.domain.writing.Description;
import kr.minimalest.api.domain.writing.Title;
import kr.minimalest.api.domain.writing.event.ArticleCompletedEvent;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PublishArticleTest {

    @InjectMocks
    PublishArticle completeArticle;

    @Mock
    ArticleRepository articleRepository;

    @Mock
    ApplicationEventPublisher eventPublisher;

    @Getter
    static class ArticleFixture {
        private final UserId userId = UserId.generate();
        private final BlogId blogId = BlogId.generate();
        private final Article article = Article.create(blogId);

        public ArticleFixture() {
            article.update(Title.of("글의 제목입니다."), Content.of("본문 내용입니다. 테스트입니다."), Description.empty());
        }

        public void updateToValidTitleAndContent() {
            article.update(Title.of("글의 제목입니다."), Content.of("본문 내용입니다. 테스트입니다."), Description.empty());
        }

        public void updateToInvalidTitleAndContent() {
            article.update(Title.of(""), Content.of(""), Description.empty());
        }

        public PublishArticleArgument getCompleteArticleArgument() {
            return PublishArticleArgument.of(article.getId());
        }
    }


    @Nested
    @DisplayName("글 완료 시")
    class HappyPublishArticle {

        @Test
        @DisplayName("글의 상태가 올바르면 PUBLISHED로 변경되고 이벤트를 발행한다")
        @Transactional
        void shouldBeCompleted() {
            // given
            ArticleFixture fixture = new ArticleFixture();
            given(articleRepository.findById(fixture.getArticle().getId())).willReturn(
                    Optional.of(fixture.article)
            );
            assertThat(fixture.article.getStatus()).isEqualTo(ArticleStatus.DRAFT);

            // when
            completeArticle.exec(fixture.getCompleteArticleArgument());

            // then
            assertThat(fixture.article.getStatus()).isEqualTo(ArticleStatus.PUBLISHED);
            assertThat(fixture.article.getPublishedAt()).isNotNull();
            verify(eventPublisher, times(1)).publishEvent(any(ArticleCompletedEvent.class));
        }

        @Test
        @DisplayName("글이 존재하지 않으면 예외가 발생한다")
        @Transactional
        void shouldThrowExceptionWhenArticleIsNotFound() {
            // given
            ArticleFixture fixture = new ArticleFixture();
            given(articleRepository.findById(fixture.getArticle().getId())).willReturn(Optional.empty());

            // when & then
            assertThrows(ArticleNotFoundException.class, () -> {
                completeArticle.exec(fixture.getCompleteArticleArgument());
            });
        }

        @Test
        @DisplayName("글이 올바르지 않으면 예외가 발생한다")
        @Transactional
        void shouldThrowExceptionWhenArticleIsInvalid() {
            // given
            ArticleFixture fixture = new ArticleFixture();
            fixture.updateToInvalidTitleAndContent();

            given(articleRepository.findById(fixture.getArticle().getId())).willReturn(
                    Optional.of(fixture.article)
            );

            // when & then
            assertThrows(ArticleCompleteFailException.class, () -> {
                completeArticle.exec(fixture.getCompleteArticleArgument());
            });
        }
    }
}
