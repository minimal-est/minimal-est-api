package kr.minimalest.api.application.reaction;

import kr.minimalest.api.domain.engagement.reaction.ReactionType;
import kr.minimalest.api.domain.engagement.reaction.repository.ArticleReactionRepository;
import kr.minimalest.api.domain.writing.ArticleId;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GetArticleReactionStatsTest {

    @InjectMocks
    GetArticleReactionStats getArticleReactionStats;

    @Mock
    ArticleReactionRepository reactionRepository;

    @Getter
    static class ReactionStatsFixture {
        private final UUID articleId = UUID.randomUUID();

        public GetArticleReactionStatsArgument getGetArticleReactionStatsArgument() {
            return GetArticleReactionStatsArgument.of(articleId);
        }

        public Map<ReactionType, Long> getReactionStats() {
            Map<ReactionType, Long> stats = new HashMap<>();
            stats.put(ReactionType.READ, 10L);
            stats.put(ReactionType.AGREE, 5L);
            stats.put(ReactionType.USEFUL, 3L);
            return stats;
        }
    }

    ReactionStatsFixture fixture = new ReactionStatsFixture();

    @Nested
    @DisplayName("반응 통계 조회 시")
    class HappyGetReactionStats {

        @Test
        @DisplayName("아티클의 반응 타입별 카운트를 반환한다")
        void shouldReturnReactionStats() {
            // given
            Map<ReactionType, Long> expectedStats = fixture.getReactionStats();
            given(reactionRepository.countActiveByArticleIdGroupByType(any(ArticleId.class)))
                .willReturn(expectedStats);

            // when
            GetArticleReactionStatsResult result = getArticleReactionStats.exec(
                fixture.getGetArticleReactionStatsArgument()
            );

            // then
            assertThat(result.stats())
                .containsEntry("READ", 10L)
                .containsEntry("AGREE", 5L)
                .containsEntry("USEFUL", 3L);
        }

        @Test
        @DisplayName("반응이 없으면 모든 카운트가 0을 반환한다")
        void shouldReturnZeroCountWhenNoReactions() {
            // given
            Map<ReactionType, Long> emptyStats = new HashMap<>();
            given(reactionRepository.countActiveByArticleIdGroupByType(any(ArticleId.class)))
                .willReturn(emptyStats);

            // when
            GetArticleReactionStatsResult result = getArticleReactionStats.exec(
                fixture.getGetArticleReactionStatsArgument()
            );

            // then
            assertThat(result.stats())
                .hasSize(3)
                .containsEntry("READ", 0L)
                .containsEntry("AGREE", 0L)
                .containsEntry("USEFUL", 0L);
        }
    }
}
