package kr.minimalest.api.application.auth;

public record JwtAuthResult(
        JwtToken accessToken,
        JwtToken refreshToken,
        JwtTokenValidityInMills refreshTokenValidityInMills
) {
    public JwtAuthResult {
        if (accessToken == null) {
            throw new IllegalArgumentException("accessToken은 null이 될 수 없습니다!");
        }

        if (refreshToken == null) {
            throw new IllegalArgumentException("refreshToken은 null이 될 수 없습니다!");
        }
    }

    public static JwtAuthResult of(JwtToken accessToken, JwtToken refreshToken, JwtTokenValidityInMills jwtTokenValidityInMills) {
        return new JwtAuthResult(accessToken, refreshToken, jwtTokenValidityInMills);
    }
}
