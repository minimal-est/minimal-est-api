package kr.minimalest.api.domain.user;

import org.springframework.util.Assert;

public record Email(String value) {

    public Email {
        int length = value.length();
        Assert.isTrue(
                5 <= length && length <= 100,
                "이메일은 5자 이상 100자 이하여야합니다."
        );
    }

    public static Email of(String value) {
        return new Email(value);
    }
}
