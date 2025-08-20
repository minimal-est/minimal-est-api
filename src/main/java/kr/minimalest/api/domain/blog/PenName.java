package kr.minimalest.api.domain.blog;

import org.springframework.util.Assert;

public record PenName(String name) {

    public PenName {
        Assert.hasText(name, "name은 값을 가져야 합니다.");
    }

    public static PenName of(String name) {
        return new PenName(name);
    }
}
