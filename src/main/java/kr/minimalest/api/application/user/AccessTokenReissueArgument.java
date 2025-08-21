package kr.minimalest.api.application.user;

import org.springframework.util.StringUtils;

public record AccessTokenReissueArgument(String refreshToken) {

    public AccessTokenReissueArgument {
        if (!StringUtils.hasText(refreshToken)) {
            throw new IllegalArgumentException("Refresh Token이 비어있습니다!");
        }
    }

    public static AccessTokenReissueArgument of(String refreshToken) {
        return new AccessTokenReissueArgument(refreshToken);
    }
}
