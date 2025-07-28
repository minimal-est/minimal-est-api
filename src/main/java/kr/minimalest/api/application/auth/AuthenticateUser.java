package kr.minimalest.api.application.auth;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Business
@RequiredArgsConstructor
public class AuthenticateUser {

    private final JwtProvider jwtProvider;
    private final UserAuthenticator userAuthenticator;

    @Transactional(readOnly = true)
    public JwtAuthResult exec(AuthenticateUserArgument argument) {
        User authenticatedUser = userAuthenticator.authenticate(
                Email.of(argument.email()),
                Password.of(argument.rawPassword())
        );
        UserUUID userUUID = authenticatedUser.userUUID();
        List<RoleType> roleTypes = authenticatedUser.roles().stream().map(Role::roleType).toList();

        JwtToken accessToken = jwtProvider.generateAccessToken(userUUID, roleTypes);
        JwtToken refreshToken = jwtProvider.generateRefreshToken(userUUID, roleTypes);

        JwtTokenValidityInMills refreshTokenValidityInMills = jwtProvider.getRefreshValidityInMills();

        return JwtAuthResult.of(accessToken, refreshToken, refreshTokenValidityInMills);
    }
}
