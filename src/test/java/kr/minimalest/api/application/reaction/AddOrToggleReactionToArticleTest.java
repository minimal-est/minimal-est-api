package kr.minimalest.api.application.reaction;

import kr.minimalest.api.domain.engagement.reaction.ArticleReaction;
import kr.minimalest.api.domain.engagement.reaction.ReactionType;
import kr.minimalest.api.domain.engagement.reaction.ReactionState;
import kr.minimalest.api.domain.engagement.reaction.repository.ArticleReactionRepository;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.access.UserId;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddOrToggleReactionToArticleTest {

    @InjectMocks
    AddOrToggleReactionToArticle addOrToggleReactionToArticle;

    @Mock
    ArticleReactionRepository reactionRepository;

    @Getter
    static class AddOrToggleReactionFixture {
        private final UUID articleId = UUID.randomUUID();
        private final UUID userId = UUID.randomUUID();
        private final ArticleId articleIdValueObject = ArticleId.of(articleId);
        private final UserId userIdValueObject = UserId.of(userId);

        public AddOrToggleReactionToArticleArgument getAddReactionArgument() {
            return AddOrToggleReactionToArticleArgument.of(articleId, userId, "READ");
        }

        public ArticleReaction createReaction() {
            return ArticleReaction.create(articleIdValueObject, userIdValueObject, ReactionType.READ);
        }
    }

    AddOrToggleReactionFixture fixture = new AddOrToggleReactionFixture();

    @Nested
    @DisplayName("반응 추가/토글 시")
    class HappyAddOrToggleReaction {

        @Test
        @DisplayName("반응이 없으면 새로운 반응을 생성하고 REACTED 상태로 저장한다")
        void shouldCreateNewReactionWhenNotExists() {
            // given
            ArticleReaction newReaction = fixture.createReaction();
            given(reactionRepository.findByArticleIdAndUserIdAndReactionType(
                any(ArticleId.class),
                any(UserId.class),
                any(ReactionType.class)
            )).willReturn(Optional.empty());
            given(reactionRepository.save(any(ArticleReaction.class))).willReturn(newReaction);

            // when
            AddOrToggleReactionToArticleResult result = addOrToggleReactionToArticle.exec(
                fixture.getAddReactionArgument()
            );

            // then
            assertThat(result.reactionId()).isNotNull();
            verify(reactionRepository, times(1)).save(any(ArticleReaction.class));
        }

        @Test
        @DisplayName("반응이 있으면 토글(REACTED → CANCELED)한다")
        void shouldToggleReactionWhenExists() {
            // given
            ArticleReaction existingReaction = fixture.createReaction();
            assertThat(existingReaction.getReactionState()).isEqualTo(ReactionState.REACTED);

            given(reactionRepository.findByArticleIdAndUserIdAndReactionType(
                any(ArticleId.class),
                any(UserId.class),
                any(ReactionType.class)
            )).willReturn(Optional.of(existingReaction));
            given(reactionRepository.save(any(ArticleReaction.class))).willReturn(existingReaction);

            // when
            AddOrToggleReactionToArticleResult result = addOrToggleReactionToArticle.exec(
                fixture.getAddReactionArgument()
            );

            // then
            assertThat(existingReaction.getReactionState()).isEqualTo(ReactionState.CANCELED);
            assertThat(result.reactionId()).isEqualTo(existingReaction.getId().id());
            verify(reactionRepository, times(1)).save(any(ArticleReaction.class));
        }

        @Test
        @DisplayName("취소된 반응을 다시 누르면 토글(CANCELED → REACTED)한다")
        void shouldToggleCanceledReactionBack() {
            // given
            ArticleReaction canceledReaction = fixture.createReaction();
            canceledReaction.toggle(); // REACTED → CANCELED
            assertThat(canceledReaction.getReactionState()).isEqualTo(ReactionState.CANCELED);

            given(reactionRepository.findByArticleIdAndUserIdAndReactionType(
                any(ArticleId.class),
                any(UserId.class),
                any(ReactionType.class)
            )).willReturn(Optional.of(canceledReaction));
            given(reactionRepository.save(any(ArticleReaction.class))).willReturn(canceledReaction);

            // when
            AddOrToggleReactionToArticleResult result = addOrToggleReactionToArticle.exec(
                fixture.getAddReactionArgument()
            );

            // then
            assertThat(canceledReaction.getReactionState()).isEqualTo(ReactionState.REACTED);
            assertThat(result.reactionId()).isEqualTo(canceledReaction.getId().id());
            verify(reactionRepository, times(1)).save(any(ArticleReaction.class));
        }
    }
}
