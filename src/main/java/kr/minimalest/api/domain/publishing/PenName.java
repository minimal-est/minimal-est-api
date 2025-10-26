package kr.minimalest.api.domain.publishing;

import org.springframework.util.Assert;

public record PenName(String value) {

    public PenName {
        Assert.hasText(value, "penName은 값을 가져야 합니다.");
    }

    public static PenName of(String value) {
        return new PenName(value);
    }
}
