package kr.minimalest.api.domain.user;

import org.springframework.util.Assert;

public record Password(String value) {

    public Password {
        Assert.hasText(value, "password는 값을 가져야합니다.");
    }

    public static Password of(String value) {
        return new Password(value);
    }
}
