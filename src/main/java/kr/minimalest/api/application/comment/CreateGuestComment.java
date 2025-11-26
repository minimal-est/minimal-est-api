package kr.minimalest.api.application.comment;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.engagement.comment.Comment;
import kr.minimalest.api.domain.engagement.comment.exception.CannotReplyToReplyException;
import kr.minimalest.api.domain.engagement.comment.exception.CommentNotFoundException;
import kr.minimalest.api.domain.engagement.comment.repository.CommentRepository;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class CreateGuestComment {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CommentDetail exec(CreateGuestCommentArgument argument) {
        // 1. 아티클 존재 확인
        articleRepository.findById(argument.articleId())
                .orElseThrow(() -> new IllegalArgumentException("아티클을 찾을 수 없습니다"));

        // 2. 대댓글인 경우 부모 댓글 확인 (2단계 제약)
        if (argument.parentCommentId() != null) {
            Comment parentComment = commentRepository.findById(argument.parentCommentId())
                    .orElseThrow(() -> new CommentNotFoundException("부모 댓글을 찾을 수 없습니다"));

            // 대댓글에는 댓글을 달 수 없음 (2단계 제약)
            if (parentComment.isReply()) {
                throw new CannotReplyToReplyException("대댓글에는 댓글을 달 수 없습니다");
            }
        }

        // 3. 비밀번호 해시
        String hashedPassword = passwordEncoder.encode(argument.guestPassword());

        // 4. 비회원 댓글 생성
        Comment comment = Comment.createByGuest(
                argument.articleId(),
                argument.parentCommentId(),
                argument.guestName(),
                argument.content(),
                hashedPassword  // 해시된 비밀번호
        );

        commentRepository.save(comment);
        return CommentDetail.from(comment, null);
    }
}
