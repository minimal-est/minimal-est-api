package kr.minimalest.api.application.user;

import kr.minimalest.api.domain.access.UserId;

public record LogoutArgument(
        UserId userId
) {
    public static LogoutArgument of(UserId userId) {
        return new LogoutArgument(userId);
    }
}
