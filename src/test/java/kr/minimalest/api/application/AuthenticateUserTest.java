package kr.minimalest.api.application;

import kr.minimalest.api.application.auth.*;
import kr.minimalest.api.application.auth.JwtAuthResult;
import kr.minimalest.api.domain.user.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserTest {

    @InjectMocks
    AuthenticateUser authenticateUser;

    @Mock
    JwtProvider jwtProvider;

    @Mock
    UserAuthenticator userAuthenticator;

    @Test
    void authenticateUser_success() {
        // given
        String email = "test@example.com";
        String rawPassword = "password123";
        AuthenticateUserArgument argument = new AuthenticateUserArgument(email, rawPassword);

        UserUUID userUUID = UserUUID.of(UUID.randomUUID().toString());
        RoleType roleType = RoleType.USER;
        Role role = Role.of(RoleId.of(1L), roleType);

        User user = User.withoutId(
                userUUID,
                Email.of(email),
                Password.of(rawPassword),
                Set.of(role),
                null,
                null
        );

        JwtToken accessToken = new JwtToken("access-token");
        JwtToken refreshToken = new JwtToken("refresh-token");
        JwtTokenValidityInMills refreshTokenValidityInMills = JwtTokenValidityInMills.of(3131);
        JwtAuthResult expectedTokens = JwtAuthResult.of(accessToken, refreshToken, refreshTokenValidityInMills);

        when(userAuthenticator.authenticate(
                Email.of(email),
                Password.of(rawPassword)
        )).thenReturn(user);

        when(jwtProvider.generateAccessToken(userUUID, List.of(roleType)))
                .thenReturn(accessToken);

        when(jwtProvider.generateRefreshToken(userUUID, List.of(roleType)))
                .thenReturn(refreshToken);

        when(jwtProvider.getRefreshValidityInMills())
                .thenReturn(refreshTokenValidityInMills);

        // when
        JwtAuthResult actualTokens = authenticateUser.exec(argument);

        // then
        assertThat(actualTokens).isEqualTo(expectedTokens);

        verify(userAuthenticator).authenticate(Email.of(email), Password.of(rawPassword));
        verify(jwtProvider).generateAccessToken(userUUID, List.of(roleType));
        verify(jwtProvider).generateRefreshToken(userUUID, List.of(roleType));
    }
}
