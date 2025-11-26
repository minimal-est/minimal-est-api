package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.comment.CommentAuthorInfo;

/**
 * 댓글 작성자 정보 응답 DTO
 * 회원 댓글일 때만 제공됨 (비회원은 null)
 */
public record CommentAuthorInfoResponse(
        String penName,
        String profileImageUrl,
        String blogUrl
) {
    public static CommentAuthorInfoResponse of(CommentAuthorInfo authorInfo) {
        if (authorInfo == null) {
            return null;
        }
        return new CommentAuthorInfoResponse(
                authorInfo.penName(),
                authorInfo.profileImageUrl(),
                authorInfo.blogUrl()
        );
    }
}
