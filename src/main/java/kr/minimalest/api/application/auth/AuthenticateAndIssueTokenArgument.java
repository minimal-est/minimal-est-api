package kr.minimalest.api.application.auth;

import org.springframework.util.StringUtils;

public record AuthenticateAndIssueTokenArgument(String email, String rawPassword) {
    public AuthenticateAndIssueTokenArgument {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("email은 비어있을 수 없습니다!");
        }

        if (!StringUtils.hasText(rawPassword)) {
            throw new IllegalArgumentException("password는 비어있을 수 없습니다!");
        }
    }

    public static AuthenticateAndIssueTokenArgument of(String email, String rawPassword) {
        return new AuthenticateAndIssueTokenArgument(email, rawPassword);
    }
}
