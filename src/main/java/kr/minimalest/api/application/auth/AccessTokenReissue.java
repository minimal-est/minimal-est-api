package kr.minimalest.api.application.auth;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.application.exception.InvalidRefreshToken;
import kr.minimalest.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;

@Business
@RequiredArgsConstructor
public class AccessTokenReissue {

    private final JwtProvider jwtProvider;
    private final RefreshTokenStore refreshTokenStore;

    /**
     * 리프레시 토큰을 검증하고, 새로운 엑세스 토큰을 발급합니다.
     * @throws InvalidRefreshToken
     */
    public IssuedAccessTokenResult exec(AccessTokenReissueArgument argument) {
        JwtToken refreshToken = JwtToken.of(argument.refreshToken());

        JwtTokenPayload payload = verifyAndGetPayload(refreshToken);

        validateRefreshTokenInStore(payload.userId(), refreshToken);

        JwtToken issuedAccessToken = jwtProvider.generateAccessToken(payload.userId(), payload.roleTypes());

        return IssuedAccessTokenResult.of(issuedAccessToken);
    }

    private JwtTokenPayload verifyAndGetPayload(JwtToken refreshToken) {
        try {
            return jwtProvider.verify(refreshToken);
        } catch (Exception e) {
            throw new InvalidRefreshToken("유효하지 않은 Refresh Token 입니다!", e);
        }
    }

    private void validateRefreshTokenInStore(UserId userId, JwtToken refreshToken) {
        JwtToken refreshTokenInStore = refreshTokenStore.find(userId)
                .orElseThrow(() -> new InvalidRefreshToken("해당 Refresh Token은 안전하지 않습니다!"));

        if (!refreshTokenInStore.equals(refreshToken)) {
            throw new InvalidRefreshToken("해당 사용자의 Refresh Token과 다릅니다!");
        }
    }
}
