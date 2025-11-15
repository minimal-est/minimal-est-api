package kr.minimalest.api.domain.writing;

import org.springframework.util.Assert;

public record Slug(String value) {
    public Slug {
        Assert.hasText(value, "slug는 값을 가져야합니다.");
    }
}
