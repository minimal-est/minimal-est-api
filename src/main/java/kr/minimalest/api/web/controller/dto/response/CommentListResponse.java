package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.comment.FindCommentsResult;

import java.util.List;

public record CommentListResponse(
        List<CommentResponse> comments
) {
    public static CommentListResponse of(FindCommentsResult result) {
        List<CommentResponse> comments = result.comments().stream()
                .map(CommentResponse::of)
                .toList();
        return new CommentListResponse(comments);
    }
}
