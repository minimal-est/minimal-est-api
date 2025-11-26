package kr.minimalest.api.application.comment;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.engagement.comment.Comment;
import kr.minimalest.api.domain.engagement.comment.CommentId;
import kr.minimalest.api.domain.engagement.comment.CommentSortType;
import kr.minimalest.api.domain.engagement.comment.repository.CommentRepository;
import kr.minimalest.api.domain.writing.ArticleId;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FindCommentsForArticleTest {

    @InjectMocks
    FindCommentsForArticle findCommentsForArticle;

    @Mock
    CommentRepository commentRepository;

    @Getter
    static class FindCommentsFixture {
        private final UUID articleId = UUID.randomUUID();
        private final UUID userId = UUID.randomUUID();
        private final UUID comment1Id = UUID.randomUUID();
        private final UUID comment2Id = UUID.randomUUID();
        private final UUID reply1Id = UUID.randomUUID();
        private final UUID reply2Id = UUID.randomUUID();

        private final ArticleId articleIdValueObject = ArticleId.of(articleId);
        private final UserId userIdValueObject = UserId.of(userId);
        private final CommentId comment1IdValueObject = CommentId.of(comment1Id);
        private final CommentId comment2IdValueObject = CommentId.of(comment2Id);
        private final CommentId reply1IdValueObject = CommentId.of(reply1Id);
        private final CommentId reply2IdValueObject = CommentId.of(reply2Id);

        public FindCommentsArgument getFindCommentsArgument() {
            return new FindCommentsArgument(articleIdValueObject, CommentSortType.LATEST, 0, 20);
        }

        public Comment createParentComment1() {
            Comment comment = Comment.createByMember(
                    articleIdValueObject,
                    null,
                    userIdValueObject,
                    "user1",
                    false,
                    "좋은 글이네요!"
            );
            // Mock용으로 ID를 고정
            try {
                var idField = Comment.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(comment, comment1IdValueObject);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return comment;
        }

        public Comment createParentComment2() {
            Comment comment = Comment.createByMember(
                    articleIdValueObject,
                    null,
                    userIdValueObject,
                    "user2",
                    false,
                    "감사합니다!"
            );
            // Mock용으로 ID를 고정
            try {
                var idField = Comment.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(comment, comment2IdValueObject);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return comment;
        }

        public Comment createReplyComment1() {
            return Comment.createByMember(
                    articleIdValueObject,
                    comment1IdValueObject,
                    userIdValueObject,
                    "user1",
                    false,
                    "대댓글 1"
            );
        }

        public Comment createReplyComment2() {
            return Comment.createByMember(
                    articleIdValueObject,
                    comment1IdValueObject,
                    userIdValueObject,
                    "user1",
                    false,
                    "대댓글 2"
            );
        }
    }

    FindCommentsFixture fixture = new FindCommentsFixture();

    @Nested
    @DisplayName("댓글 조회 시")
    class FindComments {

        @Test
        @DisplayName("부모 댓글과 대댓글을 함께 조회한다 (N+1 해결)")
        void shouldFindParentAndReplyComments() {
            // given
            Comment parent1 = fixture.createParentComment1();
            Comment parent2 = fixture.createParentComment2();
            Comment reply1 = fixture.createReplyComment1();
            Comment reply2 = fixture.createReplyComment2();

            List<Comment> parentComments = List.of(parent1, parent2);
            List<Comment> allReplies = List.of(reply1, reply2);

            given(commentRepository.findParentComments(
                    any(ArticleId.class),
                    any(CommentSortType.class),
                    anyInt(),
                    anyInt()
            )).willReturn(parentComments);

            given(commentRepository.findRepliesByParentIds(any()))
                    .willReturn(allReplies);

            // when
            FindCommentsResult result = findCommentsForArticle.exec(fixture.getFindCommentsArgument());

            // then
            assertThat(result.comments()).hasSize(2);
            // parent1이 comment1IdValueObject의 ID를 가지므로, parent1에 속한 대댓글 2개를 확인
            assertThat(result.comments().get(0).replies()).hasSize(2);
            assertThat(result.comments().get(1).replies()).isEmpty();
            verify(commentRepository, times(1)).findParentComments(
                    any(ArticleId.class),
                    any(CommentSortType.class),
                    anyInt(),
                    anyInt()
            );
            verify(commentRepository, times(1)).findRepliesByParentIds(any());
        }

        @Test
        @DisplayName("댓글이 없으면 빈 목록을 반환한다")
        void shouldReturnEmptyListWhenNoComments() {
            // given
            given(commentRepository.findParentComments(
                    any(ArticleId.class),
                    any(CommentSortType.class),
                    anyInt(),
                    anyInt()
            )).willReturn(List.of());

            // when
            FindCommentsResult result = findCommentsForArticle.exec(fixture.getFindCommentsArgument());

            // then
            assertThat(result.comments()).isEmpty();
            verify(commentRepository, times(1)).findParentComments(
                    any(ArticleId.class),
                    any(CommentSortType.class),
                    anyInt(),
                    anyInt()
            );
            verify(commentRepository, times(0)).findRepliesByParentIds(any());
        }

        @Test
        @DisplayName("대댓글이 없는 댓글만 있어도 정상 조회된다")
        void shouldFindCommentsWithoutReplies() {
            // given
            Comment parent1 = fixture.createParentComment1();
            Comment parent2 = fixture.createParentComment2();

            List<Comment> parentComments = List.of(parent1, parent2);

            given(commentRepository.findParentComments(
                    any(ArticleId.class),
                    any(CommentSortType.class),
                    anyInt(),
                    anyInt()
            )).willReturn(parentComments);

            given(commentRepository.findRepliesByParentIds(any()))
                    .willReturn(List.of());

            // when
            FindCommentsResult result = findCommentsForArticle.exec(fixture.getFindCommentsArgument());

            // then
            assertThat(result.comments()).hasSize(2);
            assertThat(result.comments().get(0).replies()).isEmpty();
            assertThat(result.comments().get(1).replies()).isEmpty();
        }
    }
}
