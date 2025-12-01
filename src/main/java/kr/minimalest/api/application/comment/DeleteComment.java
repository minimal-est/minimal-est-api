package kr.minimalest.api.application.comment;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.engagement.comment.Comment;
import kr.minimalest.api.domain.engagement.comment.exception.CommentNotFoundException;
import kr.minimalest.api.domain.engagement.comment.exception.InvalidRefreshToken;
import kr.minimalest.api.domain.engagement.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class DeleteComment {

    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void exec(DeleteCommentArgument argument) {
        // 1. 댓글 조회
        Comment comment = commentRepository.findById(argument.commentId())
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다"));

        // 2. 권한 확인
        if (argument.isGuest()) {
            // 비회원: 비밀번호로 검증
            if (!passwordEncoder.matches(argument.guestPassword(), comment.getGuestPassword())) {
                throw new InvalidRefreshToken("비밀번호가 일치하지 않습니다");
            }
        } else {
            // 회원: userId로 검증
            if (!comment.getUserId().equals(argument.userId())) {
                throw new InvalidRefreshToken("자신의 댓글만 삭제할 수 있습니다");
            }
        }

        // 3. 댓글 soft delete
        comment.delete();
        commentRepository.update(comment);
    }
}
