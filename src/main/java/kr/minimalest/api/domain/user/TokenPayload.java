package kr.minimalest.api.domain.user;

import org.springframework.util.Assert;

import java.time.Instant;
import java.util.List;

public record TokenPayload(
        UserId userId,
        List<RoleType> roleTypes,
        Instant issuedAt,
        Instant expiresAt
) {

    public TokenPayload {
        Assert.notNull(userId, "userId가 null입니다.");
        Assert.notNull(roleTypes, "RoleType 목록이 null입니다.");
        Assert.notNull(issuedAt, "issuedAt이 null입니다.");
        Assert.notNull(expiresAt, "expiresAt이 null입니다.");
        if (roleTypes.isEmpty()) {
            throw new IllegalArgumentException("RoleType 목록이 비어있습니다.");
        }
    }

    public static TokenPayload of(
            UserId userId, List<RoleType> roleTypes, Instant issuedAt, Instant expiresAt
    ) {
        return new TokenPayload(userId, roleTypes, issuedAt, expiresAt);
    }
}
