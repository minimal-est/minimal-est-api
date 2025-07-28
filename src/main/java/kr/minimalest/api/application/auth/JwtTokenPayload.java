package kr.minimalest.api.application.auth;

import kr.minimalest.api.domain.user.RoleType;
import kr.minimalest.api.domain.user.UserUUID;

import java.time.Instant;
import java.util.List;

public record JwtTokenPayload(
        UserUUID userUUID,
        List<RoleType> roleTypes,
        Instant issuedAt,
        Instant expiresAt
) {
    public JwtTokenPayload {
        if (userUUID == null)
            throw new IllegalArgumentException("JwtTokenPayload에 userUUID는 필수입니다!");

        if (roleTypes == null || roleTypes.isEmpty())
            throw new IllegalArgumentException("JwtTokenPayload에 roleIds가 비어있거나 null입니다!");

        if (issuedAt == null)
            throw new IllegalArgumentException("JwtTokenPayload의 issuedAt은 필수입니다!");

        if (expiresAt == null)
            throw new IllegalArgumentException("JwtTokenPayload의 expiresAt은 필수입니다!");

        if (expiresAt.isBefore(issuedAt))
            throw new IllegalArgumentException("expiresAt은 issuedAt 이후여야 합니다!");

        if (expiresAt.isBefore(Instant.now()))
            throw new IllegalArgumentException("JwtTokenPayload의 expiresAt은 과거값이 될 수 없습니다!");
    }
    public static JwtTokenPayload of(
            UserUUID userUUID, List<RoleType> roleTypes, Instant issuedAt, Instant expiresAt
    ) {
        return new JwtTokenPayload(userUUID, roleTypes, issuedAt, expiresAt);
    }
}
