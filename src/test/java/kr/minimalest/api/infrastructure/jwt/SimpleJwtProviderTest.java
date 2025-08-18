package kr.minimalest.api.infrastructure.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import kr.minimalest.api.application.auth.JwtTokenValidityInMills;
import kr.minimalest.api.application.auth.JwtTokenPayload;
import kr.minimalest.api.application.auth.JwtToken;
import kr.minimalest.api.domain.user.RoleType;
import kr.minimalest.api.domain.user.UserId;
import kr.minimalest.api.infrastructure.jwt.exception.JwtTokenVerifyException;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleJwtProviderTest {

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

    @Getter
    static class TokenFixture {
        private final UserId userId = UserId.of(UUID.randomUUID());
        private final List<RoleType> roleTypes = List.of(RoleType.USER, RoleType.ADMIN);
        private final JwtTokenValidityInMills validity = JwtTokenValidityInMills.ofSeconds(3600);
    }

    @Nested
    @DisplayName("토큰 증명")
    class VerifyToken {

        @Test
        @DisplayName("유효한 토큰이면 증명 된다.")
        void shouldBeVerifiedWhenTokenIsValid() {
            // given
            TokenFixture fixture = new TokenFixture();
            JwtToken jwtToken = jwtProvider.generateToken(fixture.userId, fixture.roleTypes, fixture.validity);

            // when
            JwtTokenPayload jwtTokenPayload = jwtProvider.verify(jwtToken);

            // then
            assertThat(jwtTokenPayload.userId()).isEqualTo(fixture.userId);
            assertThat(jwtTokenPayload.roleTypes()).isEqualTo(fixture.roleTypes);
            assertThat(jwtTokenPayload.expiresAt()).isAfter(Instant.now());
            assertThat(jwtTokenPayload.issuedAt()).isBefore(Instant.now());
        }

        @Test
        @DisplayName("유효하지 않은 토큰이면 예외가 발생한다")
        void shouldThrowExceptionWhenTokenIsInvalid() {
            // given
            JwtToken invalidToken = JwtToken.of("invalid-token");

            // when & then
            assertThrows(JwtTokenVerifyException.class, () ->
                    jwtProvider.verify(invalidToken)
            );
        }
    }

    @Nested
    @DisplayName("토큰 생성")
    class GenerateToken {

        @Test
        @DisplayName("토큰이 올바르게 생성되어야 한다")
        void shouldGenerateValidToken() {
            // given
            TokenFixture fixture = new TokenFixture();

            // when
            JwtToken jwtToken = jwtProvider.generateAccessToken(fixture.userId, fixture.roleTypes);

            // then
            DecodedJWT decodedJWT = JWT.decode(jwtToken.value());
            assertThat(decodedJWT.getToken()).isEqualTo(jwtToken.value());
            assertThat(decodedJWT.getSubject()).isEqualTo(fixture.userId.id().toString());
            assertThat(decodedJWT.getClaim("roles").asList(String.class))
                    .isEqualTo(fixture.roleTypes.stream().map(Enum::name).toList());
        }
    }
}
