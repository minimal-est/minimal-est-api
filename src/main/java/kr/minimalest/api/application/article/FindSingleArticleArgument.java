package kr.minimalest.api.application.article;

import org.springframework.util.Assert;

import java.util.UUID;

public record FindSingleArticleArgument(String penName, UUID articleId) {

    public FindSingleArticleArgument {
        Assert.notNull(articleId, "articleId는 null일 수 없습니다.");
    }

    public static FindSingleArticleArgument of(String penName, UUID articleId) {
        return new FindSingleArticleArgument(penName, articleId);
    }
}
