package kr.minimalest.api.application.auth;

import kr.minimalest.api.domain.user.RoleType;
import kr.minimalest.api.domain.user.UserUUID;

import java.util.List;

public interface JwtProvider {

    JwtTokenPayload verify(JwtToken jwtToken);

    JwtToken generateAccessToken(UserUUID userUUID, List<RoleType> roleTypes);

    JwtToken generateRefreshToken(UserUUID userUUID, List<RoleType> roleTypes);

    JwtToken generateToken(UserUUID userUUID, List<RoleType> roleTypes, JwtTokenValidityInMills jwtTokenValidityInMills);

    JwtTokenValidityInMills getAccessValidityInMills();

    JwtTokenValidityInMills getRefreshValidityInMills();
}
