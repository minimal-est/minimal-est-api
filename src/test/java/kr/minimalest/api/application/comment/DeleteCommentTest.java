package kr.minimalest.api.application.comment;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.engagement.comment.Comment;
import kr.minimalest.api.domain.engagement.comment.CommentId;
import kr.minimalest.api.domain.engagement.comment.CommentStatus;
import kr.minimalest.api.domain.engagement.comment.exception.CommentNotFoundException;
import kr.minimalest.api.domain.engagement.comment.exception.InvalidRefreshToken;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteCommentTest {

    @InjectMocks
    DeleteComment deleteComment;

    @Mock
    CommentRepository commentRepository;

    @Getter
    static class DeleteCommentFixture {
        private final UUID articleId = UUID.randomUUID();
        private final UUID userId = UUID.randomUUID();
        private final UUID commentId = UUID.randomUUID();
        private final UUID otherUserId = UUID.randomUUID();

        private final ArticleId articleIdValueObject = ArticleId.of(articleId);
        private final UserId userIdValueObject = UserId.of(userId);
        private final UserId otherUserIdValueObject = UserId.of(otherUserId);
        private final CommentId commentIdValueObject = CommentId.of(commentId);

        public DeleteCommentArgument getDeleteCommentArgument() {
            return DeleteCommentArgument.ofMember(commentId, userIdValueObject);
        }

        public DeleteCommentArgument getDeleteCommentArgumentByOtherUser() {
            return DeleteCommentArgument.ofMember(commentId, otherUserIdValueObject);
        }

        public Comment createComment() {
            return Comment.createByMember(
                    articleIdValueObject,
                    null,
                    userIdValueObject,
                    "testUser",
                    false,
                    "좋은 글이네요!"
            );
        }
    }

    DeleteCommentFixture fixture = new DeleteCommentFixture();

    @Nested
    @DisplayName("댓글 삭제 시")
    class DeleteCommentSuccess {

        @Test
        @DisplayName("작성자가 자신의 댓글을 정상적으로 삭제한다 (Soft Delete)")
        void shouldDeleteCommentSuccessfully() {
            // given
            Comment comment = fixture.createComment();
            given(commentRepository.findById(any(CommentId.class)))
                    .willReturn(Optional.of(comment));

            // when
            deleteComment.exec(fixture.getDeleteCommentArgument());

            // then
            assertThat(comment.getStatus()).isEqualTo(CommentStatus.DELETED);
            assertThat(comment.getDeletedAt()).isNotNull();
            verify(commentRepository, times(1)).update(any(Comment.class));
        }
    }

    @Nested
    @DisplayName("댓글 삭제 실패 시")
    class FailDeleteComment {

        @Test
        @DisplayName("댓글이 없으면 CommentNotFoundException을 던진다")
        void shouldThrowExceptionWhenCommentNotFound() {
            // given
            given(commentRepository.findById(any(CommentId.class)))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> deleteComment.exec(fixture.getDeleteCommentArgument()))
                    .isInstanceOf(CommentNotFoundException.class)
                    .hasMessage("댓글을 찾을 수 없습니다");

            verify(commentRepository, times(0)).update(any(Comment.class));
        }

        @Test
        @DisplayName("다른 사용자가 댓글을 삭제하려면 UnauthorizedException을 던진다")
        void shouldThrowExceptionWhenUserIsNotAuthor() {
            // given
            Comment comment = fixture.createComment();
            given(commentRepository.findById(any(CommentId.class)))
                    .willReturn(Optional.of(comment));

            // when & then
            assertThatThrownBy(() -> deleteComment.exec(fixture.getDeleteCommentArgumentByOtherUser()))
                    .isInstanceOf(InvalidRefreshToken.class)
                    .hasMessage("자신의 댓글만 삭제할 수 있습니다");

            verify(commentRepository, times(0)).update(any(Comment.class));
        }
    }
}
