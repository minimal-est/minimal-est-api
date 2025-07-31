package kr.minimalest.api.web.controller.dto;

import org.springframework.util.StringUtils;

public record AccessTokenResponse(String accessToken) {
    public AccessTokenResponse {
        if (!StringUtils.hasText(accessToken)) {
            throw new IllegalArgumentException("AccessToken의 값이 없습니다!");
        }
    }

    public static AccessTokenResponse of(String accessToken) {
        return new AccessTokenResponse(accessToken);
    }
}
