package kr.minimalest.api.application.auth;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Business
@RequiredArgsConstructor
public class AuthenticateAndIssueToken {

    private final JwtProvider jwtProvider;
    private final UserAuthenticator userAuthenticator;
    private final RefreshTokenStore refreshTokenStore;

    /**
     * 사용자의 이메일과 비밀번호를 검증하고, 엑세스 토큰과 리프레시 토큰을 발급합니다.
     * 발급한 리프레시 토큰을 저장합니다.
     */
    @Transactional(readOnly = true)
    public JwtAuthResult exec(AuthenticateAndIssueTokenArgument argument) {
        User authenticatedUser = userAuthenticator.authenticate(
                Email.of(argument.email()),
                Password.of(argument.rawPassword())
        );
        UserId userId = authenticatedUser.getId();
        List<RoleType> roleTypes = authenticatedUser.getRoles().stream().map(Role::getRoleType).toList();

        JwtToken accessToken = jwtProvider.generateAccessToken(userId, roleTypes);
        JwtToken refreshToken = jwtProvider.generateRefreshToken(userId, roleTypes);

        JwtTokenValidityInMills refreshTokenValidityInMills = jwtProvider.getRefreshValidityInMills();

        refreshTokenStore.put(userId, refreshToken);

        return JwtAuthResult.of(accessToken, refreshToken, refreshTokenValidityInMills);
    }
}
