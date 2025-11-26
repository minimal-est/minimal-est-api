package kr.minimalest.api.application.comment;

import kr.minimalest.api.domain.engagement.comment.Comment;
import kr.minimalest.api.domain.engagement.comment.CommentId;
import kr.minimalest.api.domain.engagement.comment.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Application 계층 내부에서 사용
 * 도메인 필드를 포함하여 비즈니스 로직에 활용
 */
public record CommentDetail(
        CommentId id,
        String authorName,
        String content,
        CommentStatus status,
        boolean isGuest,  // 비회원 댓글 여부 (회원은 false, 비회원은 true)
        CommentAuthorInfo authorInfo,  // 회원 댓글일 때만 제공 (비회원은 null)
        int likeCount,
        LocalDateTime createdAt,
        List<CommentDetail> replies  // 대댓글 목록
) {
    public static CommentDetail from(Comment comment, List<CommentDetail> replies) {
        return new CommentDetail(
                comment.getId(),
                comment.getAuthorName(),
                comment.getContent().value(),
                comment.getStatus(),
                comment.getUserId() == null,  // userId가 null이면 비회원
                null,  // authorInfo는 FindCommentsForArticle에서 설정
                comment.getLikeCount(),
                comment.getCreatedAt(),
                replies
        );
    }

    public static CommentDetail from(Comment comment, List<CommentDetail> replies,
                                     CommentAuthorInfo authorInfo) {
        return new CommentDetail(
                comment.getId(),
                comment.getAuthorName(),
                comment.getContent().value(),
                comment.getStatus(),
                comment.getUserId() == null,  // userId가 null이면 비회원
                authorInfo,
                comment.getLikeCount(),
                comment.getCreatedAt(),
                replies
        );
    }

    /**
     * 삭제된 댓글인지 확인
     */
    public boolean isDeleted() {
        return status == CommentStatus.DELETED;
    }

    /**
     * 표시할 내용 (삭제된 댓글은 "삭제된 댓글입니다" 표시)
     */
    public String displayContent() {
        return isDeleted() ? "삭제된 댓글입니다" : content;
    }
}
