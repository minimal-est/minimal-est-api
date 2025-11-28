package kr.minimalest.api.domain.access;

import org.springframework.util.Assert;

import java.util.UUID;

public record VerificationToken(
        UUID uuid
) {
    public VerificationToken {
        Assert.hasText(uuid.toString(), "uuid는 필수입니다!");
    }

    public static VerificationToken generate() {
        return new VerificationToken(UUID.randomUUID());
    }
}
