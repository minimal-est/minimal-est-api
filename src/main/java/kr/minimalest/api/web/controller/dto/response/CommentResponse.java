package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.comment.CommentDetail;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Web 계층 - 순수 POJO로 응답
 */
public record CommentResponse(
        UUID id,
        String authorName,
        String content,
        boolean isGuest,  // 비회원 댓글 여부
        CommentAuthorInfoResponse authorInfo,  // 회원 댓글일 때만 제공 (비회원은 null)
        int likeCount,
        LocalDateTime createdAt,
        List<CommentResponse> replies
) {
    public static CommentResponse of(CommentDetail commentDetail) {
        List<CommentResponse> replies = commentDetail.replies() != null
                ? commentDetail.replies().stream()
                .map(CommentResponse::of)
                .toList()
                : List.of();

        return new CommentResponse(
                commentDetail.id().id(),
                commentDetail.authorName(),
                commentDetail.displayContent(),  // 삭제된 댓글은 "삭제된 댓글입니다" 표시
                commentDetail.isGuest(),
                CommentAuthorInfoResponse.of(commentDetail.authorInfo()),
                commentDetail.likeCount(),
                commentDetail.createdAt(),
                replies
        );
    }
}
