package kr.minimalest.api.application.comment;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.engagement.comment.CommentId;

import java.util.UUID;

public record DeleteCommentArgument(
        CommentId commentId,
        UserId userId,
        String guestPassword,  // 비회원 비밀번호 (회원은 null)
        boolean isGuest
) {
    /**
     * 회원이 자신의 댓글을 삭제할 때
     */
    public static DeleteCommentArgument ofMember(
            UUID commentId,
            UserId userId
    ) {
        return new DeleteCommentArgument(
                CommentId.of(commentId),
                userId,
                null,
                false
        );
    }

    /**
     * 비회원이 자신의 댓글을 삭제할 때 (비밀번호로 인증)
     */
    public static DeleteCommentArgument ofGuest(
            UUID commentId,
            String guestPassword
    ) {
        return new DeleteCommentArgument(
                CommentId.of(commentId),
                null,
                guestPassword,
                true
        );
    }
}
