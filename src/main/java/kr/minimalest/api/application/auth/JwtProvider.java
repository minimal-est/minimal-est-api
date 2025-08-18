package kr.minimalest.api.application.auth;

import kr.minimalest.api.domain.user.RoleType;
import kr.minimalest.api.domain.user.UserId;

import java.util.List;

public interface JwtProvider {

    JwtTokenPayload verify(JwtToken jwtToken);

    JwtToken generateAccessToken(UserId userId, List<RoleType> roleTypes);

    JwtToken generateRefreshToken(UserId userId, List<RoleType> roleTypes);

    JwtToken generateToken(UserId userId, List<RoleType> roleTypes, JwtTokenValidityInMills jwtTokenValidityInMills);

    JwtTokenValidityInMills getAccessValidityInMills();

    JwtTokenValidityInMills getRefreshValidityInMills();
}
