package kr.minimalest.api.infrastructure.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import kr.minimalest.api.domain.user.*;
import kr.minimalest.api.application.exception.TokenVerificationException;
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

    JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        JwtProperties jwtProperties = new JwtProperties(
                "test-issuer",
                "secret-key",
                3600,
                12800
        );

        tokenProvider = new JwtTokenProvider(jwtProperties);
    }

    @Getter
    static class TokenFixture {
        private final UserId userId = UserId.of(UUID.randomUUID());
        private final List<RoleType> roleTypes = List.of(RoleType.USER, RoleType.ADMIN);
        private final TokenValidityInMills validity = TokenValidityInMills.ofSeconds(3600);
    }

    @Nested
    @DisplayName("토큰 증명")
    class VerifyToken {

        @Test
        @DisplayName("유효한 토큰이면 증명 된다.")
        void shouldBeVerifiedWhenTokenIsValid() {
            // given
            TokenFixture fixture = new TokenFixture();
            Token token = tokenProvider.generateToken(fixture.userId, fixture.roleTypes, fixture.validity);

            // when
            TokenPayload tokenPayload = tokenProvider.verify(token);

            // then
            assertThat(tokenPayload.userId()).isEqualTo(fixture.userId);
            assertThat(tokenPayload.roleTypes()).isEqualTo(fixture.roleTypes);
            assertThat(tokenPayload.expiresAt()).isAfter(Instant.now());
            assertThat(tokenPayload.issuedAt()).isBefore(Instant.now());
        }

        @Test
        @DisplayName("유효하지 않은 토큰이면 예외가 발생한다")
        void shouldThrowExceptionWhenTokenIsInvalid() {
            // given
            Token invalidToken = Token.of("invalid-token");

            // when & then
            assertThrows(TokenVerificationException.class, () ->
                    tokenProvider.verify(invalidToken)
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
            Token token = tokenProvider.generateAccessToken(fixture.userId, fixture.roleTypes);

            // then
            DecodedJWT decodedJWT = JWT.decode(token.value());
            assertThat(decodedJWT.getToken()).isEqualTo(token.value());
            assertThat(decodedJWT.getSubject()).isEqualTo(fixture.userId.id().toString());
            assertThat(decodedJWT.getClaim("roles").asList(String.class))
                    .isEqualTo(fixture.roleTypes.stream().map(Enum::name).toList());
        }
    }
}
