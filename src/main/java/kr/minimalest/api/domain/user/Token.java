package kr.minimalest.api.domain.user;

import org.springframework.util.Assert;

public record Token(String value) {

    public Token {
        Assert.hasText(value, "token은 값을 가져야합니다!");
    }

    public static Token of(String value) {
        return new Token(value);
    }
}
