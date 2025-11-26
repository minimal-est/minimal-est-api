package kr.minimalest.api.application.comment;

import kr.minimalest.api.domain.engagement.comment.CommentId;
import kr.minimalest.api.domain.writing.ArticleId;

import java.util.UUID;

public record CreateGuestCommentArgument(
        ArticleId articleId,
        CommentId parentCommentId,  // null이면 댓글, 값이 있으면 대댓글
        String guestName,
        String content,
        String guestPassword  // 비밀번호 (평문)
) {
    /**
     * 비회원이 댓글/대댓글을 작성할 때 호출
     */
    public static CreateGuestCommentArgument of(
            UUID articleId,
            String guestName,
            String content,
            String guestPassword,
            UUID parentCommentId
    ) {
        return new CreateGuestCommentArgument(
                ArticleId.of(articleId),
                parentCommentId != null ? CommentId.of(parentCommentId) : null,
                guestName,
                content,
                guestPassword
        );
    }
}
