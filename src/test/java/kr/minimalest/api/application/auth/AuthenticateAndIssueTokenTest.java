package kr.minimalest.api.application.auth;

import kr.minimalest.api.application.exception.AuthenticateUserException;
import kr.minimalest.api.domain.user.*;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateAndIssueTokenTest {

    @InjectMocks
    AuthenticateAndIssueToken authenticateAndIssueToken;

    @Mock
    JwtProvider jwtProvider;

    @Mock
    UserAuthenticator userAuthenticator;

    @Mock
    RefreshTokenStore refreshTokenStore;

    @Getter
    private static class AuthTokenFixture {
        private final UserUUID userUUID = UserUUID.of(UUID.randomUUID().toString());
        private final RoleType roleType = RoleType.USER;
        private final List<RoleType> roleTypes = List.of(roleType);
        private final JwtTokenValidityInMills refreshTokenValidityInMills = JwtTokenValidityInMills.ofSeconds(3600);
        private final String validEmail = "test@test.com";
        private final String invalidEmail = "test@";
        private final String validRawPassword = "test1234";
        private final String invalidRawPassword = "t";

        private final JwtToken generatedAccessToken = JwtToken.of("access-token");
        private final JwtToken generatedRefreshToken = JwtToken.of("refresh-token");

        public AuthenticateAndIssueTokenArgument getArgument() {
            return AuthenticateAndIssueTokenArgument.of(validEmail, validRawPassword);
        }

        public AuthenticateAndIssueTokenArgument getArgumentWithInvalidEmail() {
            return AuthenticateAndIssueTokenArgument.of(invalidEmail, validRawPassword);
        }

        public AuthenticateAndIssueTokenArgument getArgumentWithInvalidRawPassword() {
            return AuthenticateAndIssueTokenArgument.of(validEmail, invalidRawPassword);
        }

        public User getAuthenticatedUser() {
            return User.withoutId(
                    userUUID,
                    Email.of(validEmail),
                    Password.of(validRawPassword),
                    Set.of(Role.of(RoleId.of(1L), roleType)),
                    null,
                    null
            );
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
            given(userAuthenticator.authenticate(
                    Email.of(fixture.validEmail),
                    Password.of(fixture.validRawPassword)
            )).willReturn(fixture.getAuthenticatedUser());

            given(jwtProvider.generateAccessToken(fixture.userUUID, fixture.roleTypes)).willReturn(fixture.generatedAccessToken);
            given(jwtProvider.generateRefreshToken(fixture.userUUID, fixture.roleTypes)).willReturn(fixture.generatedRefreshToken);
            given(jwtProvider.getRefreshValidityInMills()).willReturn(fixture.getRefreshTokenValidityInMills());

            // when
            JwtAuthResult result = authenticateAndIssueToken.exec(fixture.getArgument());

            // then
            verify(refreshTokenStore).put(fixture.userUUID, fixture.generatedRefreshToken);
            assertThat(result.accessToken()).isEqualTo(fixture.generatedAccessToken);
            assertThat(result.refreshToken()).isEqualTo(fixture.generatedRefreshToken);
            assertThat(result.refreshTokenValidityInMills()).isEqualTo(fixture.getRefreshTokenValidityInMills());
        }

        @Test
        @DisplayName("이메일이 존재하지 않으면 예외가 발생한다")
        void shouldThrowExceptionWhenEmailNotFound() {
            // given
            AuthTokenFixture fixture = new AuthTokenFixture();

            given(userAuthenticator.authenticate(
                    Email.of(fixture.invalidEmail),
                    Password.of(fixture.validRawPassword)
            )).willThrow(AuthenticateUserException.class);

            // when & then
            assertThrows(AuthenticateUserException.class, () ->
                    authenticateAndIssueToken.exec(fixture.getArgumentWithInvalidEmail())
            );
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다")
        void shouldThrowExceptionWhenPasswordMismatch() {
            // given
            AuthTokenFixture fixture = new AuthTokenFixture();

            given(userAuthenticator.authenticate(
                    Email.of(fixture.validEmail),
                    Password.of(fixture.invalidRawPassword)
            )).willThrow(AuthenticateUserException.class);

            // when & then
            assertThrows(AuthenticateUserException.class, () ->
                    authenticateAndIssueToken.exec(fixture.getArgumentWithInvalidRawPassword())
            );
        }
    }
}
