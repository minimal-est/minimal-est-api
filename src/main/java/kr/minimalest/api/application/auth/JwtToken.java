package kr.minimalest.api.application.auth;

import org.springframework.util.StringUtils;

public record JwtToken(
        String value
) {
    public static JwtToken of(String value) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("JwtToken은 빈 값이 될 수 없습니다!");
        }
        return new JwtToken(value);
    }
}
