package kr.minimalest.api.application.auth;

public record IssuedAccessTokenResult(JwtToken accessToken) {

    public IssuedAccessTokenResult {
        if (accessToken == null) {
            throw new IllegalArgumentException("AccessToken이 null 입니다!");
        }
    }

    public static IssuedAccessTokenResult of(JwtToken accessToken) {
        return new IssuedAccessTokenResult(accessToken);
    }
}
