package kr.minimalest.api.infrastructure.jwt;

import kr.minimalest.api.application.JwtExpiration;
import kr.minimalest.api.application.JwtPayload;
import kr.minimalest.api.application.JwtToken;
import kr.minimalest.api.domain.user.RoleType;
import kr.minimalest.api.domain.user.UserUUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class Auth0JwtProviderTest {

    SimpleJwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        JwtProperties jwtProperties = new JwtProperties(
                "test-issuer",
                "secret-key",
                3600,
                12800
        );

        jwtProvider = new SimpleJwtProvider(jwtProperties);
    }

    @Test
    void generate_token_and_verify_success() {
        // given
        UserUUID userUUID = UserUUID.of(UUID.randomUUID().toString());
        List<RoleType> roleTypes = List.of(RoleType.USER, RoleType.ADMIN);
        JwtExpiration expiration = JwtExpiration.ofSeconds(3600);

        // when
        JwtToken jwtToken = jwtProvider.generateToken(userUUID, roleTypes, expiration);
        JwtPayload jwtPayload = jwtProvider.verify(jwtToken);

        // then
        assertThat(jwtPayload.userUUID()).isEqualTo(userUUID);
        assertThat(jwtPayload.roleTypes()).isEqualTo(roleTypes);
        assertThat(jwtPayload.expiresAt()).isAfter(Instant.now());
        assertThat(jwtPayload.issuedAt()).isBefore(Instant.now());
    }
}
