package kr.minimalest.api.application.auth;

import kr.minimalest.api.domain.access.exception.AuthenticateUserException;
import kr.minimalest.api.application.user.AuthenticateAndIssueToken;
import kr.minimalest.api.application.user.AuthenticateAndIssueTokenArgument;
import kr.minimalest.api.application.user.AuthenticateAndIssueTokenResult;
import kr.minimalest.api.domain.access.*;
import kr.minimalest.api.domain.access.service.TokenProvider;
import kr.minimalest.api.domain.access.service.UserAuthenticator;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateAndIssueTokenTest {

    @InjectMocks
    AuthenticateAndIssueToken authenticateAndIssueToken;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    UserAuthenticator userAuthenticator;

    @Mock
    RefreshTokenStore refreshTokenStore;

    @Getter
    private static class AuthTokenFixture {
        private final TokenValidityInMills refreshTokenValidityInMills = TokenValidityInMills.ofSeconds(3600);
        private final String validEmail = "test@test.com";
        private final String validRawPassword = "test1234";

        private final Token generatedAccessToken = Token.of("access-token");
        private final Token generatedRefreshToken = Token.of("refresh-token");

        public AuthenticateAndIssueTokenArgument getArgument() {
            return AuthenticateAndIssueTokenArgument.of(validEmail, validRawPassword);
        }

        public User createAuthenticatedUser() {
            return User.signUp(Email.of(validEmail), Password.of(validRawPassword));
        }
    }

    @Nested
    @DisplayName("사용자 인증 및 토큰 발급 시")
    class IssueTokens {

        @Test
        @DisplayName("유효한 이메일과 비밀번호로 인증하면 토큰을 발급한다")
        void shouldIssueTokensWhenCredentialsAreValid() {
            // given
            AuthTokenFixture fixture = new AuthTokenFixture();

            User authenticatedUser = fixture.createAuthenticatedUser();

            given(userAuthenticator.authenticate(
                    Email.of(fixture.validEmail),
                    Password.of(fixture.validRawPassword)
            )).willReturn(authenticatedUser);

            given(tokenProvider.generateAccessToken(authenticatedUser.getId(), authenticatedUser.getRoleTypes())).willReturn(fixture.generatedAccessToken);
            given(tokenProvider.generateRefreshToken(authenticatedUser.getId(), authenticatedUser.getRoleTypes())).willReturn(fixture.generatedRefreshToken);
            given(tokenProvider.getRefreshValidityInMills()).willReturn(fixture.getRefreshTokenValidityInMills());

            // when
            AuthenticateAndIssueTokenResult result = authenticateAndIssueToken.exec(fixture.getArgument());

            // then
            verify(refreshTokenStore).put(authenticatedUser.getId(), fixture.generatedRefreshToken);
            assertThat(result.accessToken()).isEqualTo(fixture.generatedAccessToken);
            assertThat(result.refreshToken()).isEqualTo(fixture.generatedRefreshToken);
            assertThat(result.refreshTokenValidityInMills()).isEqualTo(fixture.getRefreshTokenValidityInMills());
        }

        @Test
        @DisplayName("인증에 통과하지 못하면 예외가 발생한다")
        void shouldThrowExceptionWhenAuthenticationFailed() {
            // given
            AuthTokenFixture fixture = new AuthTokenFixture();

            given(userAuthenticator.authenticate(
                    Email.of(fixture.validEmail),
                    Password.of(fixture.validRawPassword)
            )).willThrow(AuthenticateUserException.class);

            // when & then
            assertThrows(AuthenticateUserException.class, () ->
                    authenticateAndIssueToken.exec(fixture.getArgument())
            );
        }
    }
}
