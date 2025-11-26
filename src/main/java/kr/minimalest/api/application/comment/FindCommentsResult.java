package kr.minimalest.api.application.comment;

import java.util.List;

public record FindCommentsResult(
        List<CommentDetail> comments
) {
    public static FindCommentsResult of(List<CommentDetail> comments) {
        return new FindCommentsResult(comments);
    }
}
