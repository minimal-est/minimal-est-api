package kr.minimalest.api.domain.user.service;

import kr.minimalest.api.domain.user.*;

import java.util.List;

public interface TokenProvider {

    TokenPayload verify(Token token);

    Token generateAccessToken(UserId userId, List<RoleType> roleTypes);

    Token generateRefreshToken(UserId userId, List<RoleType> roleTypes);

    Token generateToken(UserId userId, List<RoleType> roleTypes, TokenValidityInMills jwtTokenValidityInMills);

    TokenValidityInMills getAccessValidityInMills();

    TokenValidityInMills getRefreshValidityInMills();
}
