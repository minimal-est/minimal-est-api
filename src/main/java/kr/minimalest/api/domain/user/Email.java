package kr.minimalest.api.domain.user;

import org.springframework.util.Assert;

public record Email(String value) {

    public Email {
        Assert.hasText(value, "email은 공백일 수 없습니다.");
    }

    public static Email of(String value) {
        return new Email(value);
    }
}
