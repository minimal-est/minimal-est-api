package kr.minimalest.api.application.comment;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.engagement.comment.CommentId;
import kr.minimalest.api.domain.writing.ArticleId;

import java.util.UUID;

public record CreateCommentArgument(
        ArticleId articleId,
        CommentId parentCommentId,  // null이면 댓글, 값이 있으면 대댓글
        UserId userId,
        String penName,
        boolean isAnonymous,
        String content
) {
    /**
     * 회원이 댓글/대댓글을 작성할 때 호출
     */
    public static CreateCommentArgument ofMember(
            UUID articleId,
            UserId userId,
            String penName,
            String content,
            boolean isAnonymous,
            UUID parentCommentId
    ) {
        return new CreateCommentArgument(
                ArticleId.of(articleId),
                parentCommentId != null ? CommentId.of(parentCommentId) : null,
                userId,
                penName,
                isAnonymous,
                content
        );
    }
}
