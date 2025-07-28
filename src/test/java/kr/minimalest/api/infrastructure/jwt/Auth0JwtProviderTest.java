package kr.minimalest.api.infrastructure.jwt;

import kr.minimalest.api.application.auth.JwtTokenValidityInMills;
import kr.minimalest.api.application.auth.JwtTokenPayload;
import kr.minimalest.api.application.auth.JwtToken;
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
        JwtTokenValidityInMills expiration = JwtTokenValidityInMills.ofSeconds(3600);

        // when
        JwtToken jwtToken = jwtProvider.generateToken(userUUID, roleTypes, expiration);
        JwtTokenPayload jwtTokenPayload = jwtProvider.verify(jwtToken);

        // then
        assertThat(jwtTokenPayload.userUUID()).isEqualTo(userUUID);
        assertThat(jwtTokenPayload.roleTypes()).isEqualTo(roleTypes);
        assertThat(jwtTokenPayload.expiresAt()).isAfter(Instant.now());
        assertThat(jwtTokenPayload.issuedAt()).isBefore(Instant.now());
    }
}
