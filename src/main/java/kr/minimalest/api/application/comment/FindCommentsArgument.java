package kr.minimalest.api.application.comment;

import kr.minimalest.api.domain.engagement.comment.CommentSortType;
import kr.minimalest.api.domain.writing.ArticleId;
import org.springframework.util.Assert;

import java.util.UUID;

public record FindCommentsArgument(
        ArticleId articleId,
        CommentSortType sortType,
        int page,
        int limit
) {
    public FindCommentsArgument {
        Assert.notNull(articleId, "articleId는 null이 될 수 없습니다");
        Assert.notNull(sortType, "sortType은 null이 될 수 없습니다");
        Assert.isTrue(page >= 0, "page는 0 이상이어야 합니다");
        Assert.isTrue(limit > 0, "limit은 0보다 커야 합니다");
    }

    public static FindCommentsArgument of(
            UUID articleId,
            String sortType,
            int page,
            int limit
    ) {
        CommentSortType sort = CommentSortType.valueOf(sortType.toUpperCase());
        return new FindCommentsArgument(
                ArticleId.of(articleId),
                sort,
                page,
                limit
        );
    }
}
