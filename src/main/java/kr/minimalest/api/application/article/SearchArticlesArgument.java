package kr.minimalest.api.application.article;

import org.springframework.util.Assert;

public record SearchArticlesArgument(String query, int page, int size) {

    public SearchArticlesArgument {
        Assert.hasText(query, "query는 필수입니다");
        Assert.state(page >= 0, "page는 음수가 될 수 없습니다");
        Assert.state(size > 0, "size는 0보다 커야합니다");
    }

    public static SearchArticlesArgument of(String query, int page, int size) {
        return new SearchArticlesArgument(query, page, size);
    }
}
