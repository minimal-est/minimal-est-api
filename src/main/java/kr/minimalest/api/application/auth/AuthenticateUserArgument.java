package kr.minimalest.api.application.auth;

import org.springframework.util.StringUtils;

public record AuthenticateUserArgument(String email, String rawPassword) {
    public AuthenticateUserArgument {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("email은 비어있을 수 없습니다!");
        }

        if (!StringUtils.hasText(rawPassword)) {
            throw new IllegalArgumentException("password는 비어있을 수 없습니다!");
        }
    }

    public static AuthenticateUserArgument of(String email, String rawPassword) {
        return new AuthenticateUserArgument(email, rawPassword);
    }
}
