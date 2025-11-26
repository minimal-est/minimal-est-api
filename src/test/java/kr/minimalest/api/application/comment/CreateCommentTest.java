package kr.minimalest.api.application.comment;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.engagement.comment.Comment;
import kr.minimalest.api.domain.engagement.comment.CommentId;
import kr.minimalest.api.domain.engagement.comment.exception.CannotReplyToReplyException;
import kr.minimalest.api.domain.engagement.comment.exception.CommentNotFoundException;
import kr.minimalest.api.domain.engagement.comment.repository.CommentRepository;
import kr.minimalest.api.domain.publishing.Author;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCommentTest {

    @InjectMocks
    CreateComment createComment;

    @Mock
    CommentRepository commentRepository;

    @Mock
    ArticleRepository articleRepository;

    @Mock
    BlogRepository blogRepository;

    @Getter
    static class CreateCommentFixture {
        private final UUID articleId = UUID.randomUUID();
        private final UUID userId = UUID.randomUUID();
        private final UUID commentId = UUID.randomUUID();

        private final ArticleId articleIdValueObject = ArticleId.of(articleId);
        private final UserId userIdValueObject = UserId.of(userId);
        private final CommentId commentIdValueObject = CommentId.of(commentId);
        private final String penName = "testUser";

        public CreateCommentArgument getCreateCommentArgument() {
            return CreateCommentArgument.ofMember(
                    articleId,
                    userIdValueObject,
                    null,  // penName은 BlogRepository에서 조회
                    "좋은 글이네요!",
                    false,
                    null   // 부모 댓글 없음
            );
        }

        public CreateCommentArgument getCreateReplyArgument() {
            return CreateCommentArgument.ofMember(
                    articleId,
                    userIdValueObject,
                    null,
                    "감사합니다!",
                    false,
                    commentId  // 부모 댓글 있음
            );
        }

        public Comment createParentComment() {
            return Comment.createByMember(
                    articleIdValueObject,
                    null,
                    userIdValueObject,
                    penName,
                    false,
                    "좋은 글이네요!"
            );
        }

        public Comment createReplyComment() {
            return Comment.createByMember(
                    articleIdValueObject,
                    commentIdValueObject,
                    userIdValueObject,
                    penName,
                    false,
                    "감사합니다!"
            );
        }

        public Author mockAuthor() {
            Author author = mock(Author.class);
            PenName penNameValue = PenName.of(penName);
            when(author.getPenName()).thenReturn(penNameValue);
            return author;
        }
    }

    CreateCommentFixture fixture = new CreateCommentFixture();

    @Nested
    @DisplayName("회원 댓글 작성 시")
    class CreateMemberComment {

        @Test
        @DisplayName("부모 댓글을 정상적으로 생성한다")
        void shouldCreateParentComment() {
            // given
            Article mockArticle = mock(Article.class);
            Author author = fixture.mockAuthor();

            given(articleRepository.findById(any(ArticleId.class))).willReturn(Optional.of(mockArticle));
            given(blogRepository.findAuthorByUserId(any(UserId.class))).willReturn(Optional.of(author));

            // when
            CommentDetail result = createComment.exec(fixture.getCreateCommentArgument());

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isNotNull();
            assertThat(result.authorName()).isEqualTo(fixture.penName);
            verify(commentRepository, times(1)).save(any(Comment.class));
            verify(articleRepository, times(1)).findById(any(ArticleId.class));
            verify(blogRepository, times(1)).findAuthorByUserId(any(UserId.class));
        }

        @Test
        @DisplayName("대댓글을 정상적으로 생성한다")
        void shouldCreateReplyComment() {
            // given
            Comment parentComment = fixture.createParentComment();
            Article mockArticle = mock(Article.class);
            Author author = fixture.mockAuthor();

            given(articleRepository.findById(any(ArticleId.class))).willReturn(Optional.of(mockArticle));
            given(commentRepository.findById(any(CommentId.class))).willReturn(Optional.of(parentComment));
            given(blogRepository.findAuthorByUserId(any(UserId.class))).willReturn(Optional.of(author));

            // when
            CommentDetail result = createComment.exec(fixture.getCreateReplyArgument());

            // then
            assertThat(result).isNotNull();
            assertThat(result.id()).isNotNull();
            assertThat(result.authorName()).isEqualTo(fixture.penName);
            verify(commentRepository, times(1)).save(any(Comment.class));
            verify(articleRepository, times(1)).findById(any(ArticleId.class));
            verify(commentRepository, times(1)).findById(any(CommentId.class));
            verify(blogRepository, times(1)).findAuthorByUserId(any(UserId.class));
        }
    }

    @Nested
    @DisplayName("댓글 작성 실패 시")
    class FailCreateComment {

        @Test
        @DisplayName("아티클이 없으면 IllegalArgumentException을 던진다")
        void shouldThrowExceptionWhenArticleNotFound() {
            // given
            given(articleRepository.findById(any(ArticleId.class))).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> createComment.exec(fixture.getCreateCommentArgument()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("아티클을 찾을 수 없습니다");

            verify(commentRepository, times(0)).save(any(Comment.class));
        }

        @Test
        @DisplayName("부모 댓글이 없으면 CommentNotFoundException을 던진다")
        void shouldThrowExceptionWhenParentCommentNotFound() {
            // given
            Article mockArticle = mock(Article.class);
            given(articleRepository.findById(any(ArticleId.class))).willReturn(Optional.of(mockArticle));
            given(commentRepository.findById(any(CommentId.class))).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> createComment.exec(fixture.getCreateReplyArgument()))
                    .isInstanceOf(CommentNotFoundException.class)
                    .hasMessage("부모 댓글을 찾을 수 없습니다");

            verify(commentRepository, times(0)).save(any(Comment.class));
            verify(blogRepository, times(0)).findAuthorByUserId(any(UserId.class));
        }

        @Test
        @DisplayName("대댓글에는 댓글을 달 수 없다 (2단계 제약)")
        void shouldThrowExceptionWhenReplyingToReply() {
            // given
            Comment replyComment = fixture.createReplyComment();  // 이미 대댓글 (parentId != null)
            Article mockArticle = mock(Article.class);

            given(articleRepository.findById(any(ArticleId.class))).willReturn(Optional.of(mockArticle));
            given(commentRepository.findById(any(CommentId.class))).willReturn(Optional.of(replyComment));

            // when & then
            assertThatThrownBy(() -> createComment.exec(fixture.getCreateReplyArgument()))
                    .isInstanceOf(CannotReplyToReplyException.class)
                    .hasMessage("대댓글에는 댓글을 달 수 없습니다");

            verify(commentRepository, times(0)).save(any(Comment.class));
            verify(blogRepository, times(0)).findAuthorByUserId(any(UserId.class));
        }

        @Test
        @DisplayName("작성자 정보가 없으면 IllegalArgumentException을 던진다")
        void shouldThrowExceptionWhenAuthorNotFound() {
            // given
            Article mockArticle = mock(Article.class);
            given(articleRepository.findById(any(ArticleId.class))).willReturn(Optional.of(mockArticle));
            given(blogRepository.findAuthorByUserId(any(UserId.class))).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> createComment.exec(fixture.getCreateCommentArgument()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("작성자 정보를 찾을 수 없습니다");

            verify(commentRepository, times(0)).save(any(Comment.class));
        }
    }
}
