package kr.minimalest.api.domain.user;

import org.springframework.util.Assert;

public record Password(String value) {

    public Password {
        int length = value.length();
        Assert.isTrue(
            8 <= length && length <= 100,
            "비밀번호는 8자 이상 100자 이하여야합니다."
        );
    }

    public static Password of(String value) {
        return new Password(value);
    }
}
