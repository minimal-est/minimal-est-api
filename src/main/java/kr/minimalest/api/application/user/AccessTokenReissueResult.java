package kr.minimalest.api.application.user;

import kr.minimalest.api.domain.access.Token;
import kr.minimalest.api.domain.access.TokenValidityInMills;

public record AccessTokenReissueResult(
        Token accessToken,
        Token newRefreshToken,
        TokenValidityInMills refreshTokenValidityInMills
) {

    public AccessTokenReissueResult {
        if (accessToken == null) {
            throw new IllegalArgumentException("AccessToken이 null 입니다!");
        }
        if (newRefreshToken == null) {
            throw new IllegalArgumentException("NewRefreshToken이 null 입니다!");
        }
        if (refreshTokenValidityInMills == null) {
            throw new IllegalArgumentException("RefreshTokenValidityInMills가 null 입니다!");
        }
    }

    public static AccessTokenReissueResult of(
            Token accessToken,
            Token newRefreshToken,
            TokenValidityInMills refreshTokenValidityInMills
    ) {
        return new AccessTokenReissueResult(accessToken, newRefreshToken, refreshTokenValidityInMills);
    }
}
