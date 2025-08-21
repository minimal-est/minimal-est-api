package kr.minimalest.api.application.user;

import kr.minimalest.api.domain.user.Token;

public record AccessTokenReissueResult(Token accessToken) {

    public AccessTokenReissueResult {
        if (accessToken == null) {
            throw new IllegalArgumentException("AccessToken이 null 입니다!");
        }
    }

    public static AccessTokenReissueResult of(Token accessToken) {
        return new AccessTokenReissueResult(accessToken);
    }
}
