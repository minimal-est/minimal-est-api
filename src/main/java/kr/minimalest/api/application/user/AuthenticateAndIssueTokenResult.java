package kr.minimalest.api.application.user;

import kr.minimalest.api.domain.user.Token;
import kr.minimalest.api.domain.user.TokenValidityInMills;

public record AuthenticateAndIssueTokenResult(
        Token accessToken,
        Token refreshToken,
        TokenValidityInMills refreshTokenValidityInMills
) {
    public AuthenticateAndIssueTokenResult {
        if (accessToken == null) {
            throw new IllegalArgumentException("accessToken은 null이 될 수 없습니다!");
        }

        if (refreshToken == null) {
            throw new IllegalArgumentException("refreshToken은 null이 될 수 없습니다!");
        }
    }

    public static AuthenticateAndIssueTokenResult of(Token accessToken, Token refreshToken, TokenValidityInMills tokenValidityInMills) {
        return new AuthenticateAndIssueTokenResult(accessToken, refreshToken, tokenValidityInMills);
    }
}
