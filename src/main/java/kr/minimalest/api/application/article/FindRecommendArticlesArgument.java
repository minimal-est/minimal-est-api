package kr.minimalest.api.application.article;

import org.springframework.util.Assert;

public record FindRecommendArticlesArgument(int page, int limit) {

    public FindRecommendArticlesArgument {
        Assert.state(page >= 0, "page는 음수가 될 수 없습니다!");
        Assert.state(limit > 0, "limit은 0보다 커야합니다!");
    }

    public static FindRecommendArticlesArgument limitOf(int page, int limit) {
        return new FindRecommendArticlesArgument(page, limit);
    }
}
