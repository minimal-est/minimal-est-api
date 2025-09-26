package kr.minimalest.api.application.auth;

import kr.minimalest.api.domain.access.exception.InvalidRefreshToken;
import kr.minimalest.api.application.user.AccessTokenReissue;
import kr.minimalest.api.application.user.AccessTokenReissueArgument;
import kr.minimalest.api.application.user.AccessTokenReissueResult;
import kr.minimalest.api.domain.access.*;
import kr.minimalest.api.domain.access.service.TokenProvider;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AccessTokenReissueTest {

    @InjectMocks
    AccessTokenReissue accessTokenReissue;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    RefreshTokenStore refreshTokenStore;

    @Getter
    static class TokenFixture {
        private final UserId userId = UserId.generate();
        private final List<RoleType> roleTypes = List.of(RoleType.USER);
        private final Token validRefreshToken = Token.of("valid-refresh-token");
        private final Token issuedAccessToken = Token.of("issued-access-token");
        private final Token validRefreshTokenInStore = Token.of("valid-refresh-token");

        public AccessTokenReissueArgument getArgument() {
            return AccessTokenReissueArgument.of(validRefreshToken.value());
        }

        public Token getRefreshTokenForVerification() {
            return Token.of(getArgument().refreshToken());
        }


        public TokenPayload getTokenPayload() {
            return TokenPayload.of(
                    userId,
                    roleTypes,
                    Instant.now(),
                    Instant.now().plusSeconds(3600)
            );
        }
    }

    @Nested
    @DisplayName("엑세스 토큰 재발급시")
    class ReissueAccessToken {

        @Test
        @DisplayName("유효한 리프레시 토큰으로 새로운 엑세스 토큰을 발급한다")
        void shouldReissueAccessTokenWithValidRefreshToken() {
            // given
            TokenFixture fixture = new TokenFixture();

            given(tokenProvider.verify(fixture.getRefreshTokenForVerification())).willReturn(fixture.getTokenPayload());
            given(refreshTokenStore.find(fixture.userId)).willReturn(Optional.of(fixture.getValidRefreshTokenInStore()));
            given(tokenProvider.generateAccessToken(fixture.userId, fixture.roleTypes)).willReturn(fixture.getIssuedAccessToken());

            // when
            AccessTokenReissueResult result = accessTokenReissue.exec(fixture.getArgument());

            // then
            assertThat(result.accessToken()).isEqualTo(fixture.issuedAccessToken);
        }

        @Test
        @DisplayName("리프레시 토큰 검증 실패 시 예외가 발생한다")
        void shouldThrowExceptionWhenRefreshTokenVerificationFails() {
            // given
            TokenFixture fixture = new TokenFixture();

            given(tokenProvider.verify(fixture.getRefreshTokenForVerification()))
                    .willThrow(RuntimeException.class);

            // when & then
            assertThrows(InvalidRefreshToken.class, () ->
                    accessTokenReissue.exec(fixture.getArgument())
            );
        }

        @Test
        @DisplayName("저장소에서 리프레시 토큰을 찾을 수 없으면 예외가 발생한다")
        void shouldThrowExceptionWhenRefreshTokenNotFoundInStore() {
            // given
            TokenFixture fixture = new TokenFixture();

            given(tokenProvider.verify(fixture.getRefreshTokenForVerification())).willReturn(fixture.getTokenPayload());
            given(refreshTokenStore.find(fixture.userId)).willReturn(Optional.empty());

            // when & then
            assertThrows(InvalidRefreshToken.class, () ->
                    accessTokenReissue.exec(fixture.getArgument())
            );
        }
    }
}
