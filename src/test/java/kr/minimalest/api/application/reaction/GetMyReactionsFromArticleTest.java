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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GetMyReactionsFromArticleTest {

    @InjectMocks
    GetMyReactionsFromArticle getMyReactionsFromArticle;

    @Mock
    ArticleReactionRepository reactionRepository;

    @Getter
    static class MyReactionsFixture {
        private final UUID articleId = UUID.randomUUID();
        private final UUID userId = UUID.randomUUID();
        private final ArticleId articleIdValueObject = ArticleId.of(articleId);
        private final UserId userIdValueObject = UserId.of(userId);

        public GetMyReactionsFromArticleArgument getGetMyReactionsFromArticleArgument() {
            return GetMyReactionsFromArticleArgument.of(articleId, userId);
        }

        public List<ArticleReaction> getMyReactions() {
            ArticleReaction reaction1 = ArticleReaction.create(articleIdValueObject, userIdValueObject, ReactionType.READ);
            ArticleReaction reaction2 = ArticleReaction.create(articleIdValueObject, userIdValueObject, ReactionType.AGREE);
            return List.of(reaction1, reaction2);
        }

        public List<ArticleReaction> getEmptyReactions() {
            return List.of();
        }
    }

    MyReactionsFixture fixture = new MyReactionsFixture();

    @Nested
    @DisplayName("내 반응 조회 시")
    class HappyGetMyReactions {

        @Test
        @DisplayName("사용자의 모든 활성 반응을 반환한다")
        void shouldReturnAllActiveReactions() {
            // given
            List<ArticleReaction> reactions = fixture.getMyReactions();
            given(reactionRepository.findActiveByArticleIdAndUserId(
                any(ArticleId.class),
                any(UserId.class)
            )).willReturn(reactions);

            // when
            GetMyReactionsFromArticleResult result = getMyReactionsFromArticle.exec(
                fixture.getGetMyReactionsFromArticleArgument()
            );

            // then
            assertThat(result.reactions()).hasSize(2);
            assertThat(result.reactions())
                .extracting(MyReactionDetail::reactionType)
                .containsExactly("READ", "AGREE");
            assertThat(result.reactions())
                .extracting(MyReactionDetail::reactionState)
                .containsExactly("REACTED", "REACTED");
        }

        @Test
        @DisplayName("반응이 없으면 빈 리스트를 반환한다")
        void shouldReturnEmptyListWhenNoReactions() {
            // given
            given(reactionRepository.findActiveByArticleIdAndUserId(
                any(ArticleId.class),
                any(UserId.class)
            )).willReturn(fixture.getEmptyReactions());

            // when
            GetMyReactionsFromArticleResult result = getMyReactionsFromArticle.exec(
                fixture.getGetMyReactionsFromArticleArgument()
            );

            // then
            assertThat(result.reactions()).isEmpty();
        }
    }
}
