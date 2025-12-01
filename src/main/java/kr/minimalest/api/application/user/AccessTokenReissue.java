package kr.minimalest.api.application.user;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.access.exception.InvalidRefreshToken;
import kr.minimalest.api.domain.access.RefreshTokenStore;
import kr.minimalest.api.domain.access.Token;
import kr.minimalest.api.domain.access.TokenPayload;
import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.access.service.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class AccessTokenReissue {

    private final TokenProvider tokenProvider;
    private final RefreshTokenStore refreshTokenStore;

    /**
     * 리프레시 토큰을 검증하고, 새로운 엑세스 토큰을 발급합니다.
     * @throws InvalidRefreshToken
     */
    @Transactional(readOnly = true)
    public AccessTokenReissueResult exec(AccessTokenReissueArgument argument) {
        Token refreshToken = Token.of(argument.refreshToken());
        TokenPayload payload = verifyAndGetPayload(refreshToken);

        validateRefreshTokenInStore(payload.userId(), refreshToken);

        Token issuedAccessToken = tokenProvider.generateAccessToken(payload.userId(), payload.roleTypes());

        return AccessTokenReissueResult.of(issuedAccessToken);
    }

    private TokenPayload verifyAndGetPayload(Token refreshToken) {
        try {
            return tokenProvider.verify(refreshToken);
        } catch (Exception e) {
            throw new InvalidRefreshToken("유효하지 않은 Refresh Token 입니다!", e);
        }
    }

    private void validateRefreshTokenInStore(UserId userId, Token refreshToken) {
        Token refreshTokenInStore = refreshTokenStore.find(userId)
                .orElseThrow(() -> new InvalidRefreshToken("해당 Refresh Token은 안전하지 않습니다!"));

        if (!refreshTokenInStore.equals(refreshToken)) {
            throw new InvalidRefreshToken("해당 사용자의 Refresh Token과 다릅니다!");
        }
    }
}
