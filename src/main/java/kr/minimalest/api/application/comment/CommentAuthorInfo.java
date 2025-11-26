package kr.minimalest.api.application.comment;

/**
 * 댓글 작성자 정보 (회원 댓글일 때만 제공)
 * 프론트엔드에서 작성자 블로그로 이동하거나 프로필 정보를 표시할 때 사용
 */
public record CommentAuthorInfo(
        String penName,
        String profileImageUrl,
        String blogUrl
) {}
