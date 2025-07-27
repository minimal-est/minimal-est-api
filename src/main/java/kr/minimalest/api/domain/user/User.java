package kr.minimalest.api.domain.user;

import java.time.LocalDateTime;
import java.util.Set;

public record User(

        UserId userId,

        UserUUID userUUID,

        Email email,

        Password password,

        Set<Role> roles,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
    public static User of(
            UserId userId,
            UserUUID userUUID,
            Email email,
            Password password,
            Set<Role> roles,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new User(userId, userUUID, email, password, roles, createdAt, updatedAt);
    }

    public static User withoutId(
            UserUUID userUUID,
            Email email,
            Password password,
            Set<Role> roles,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return User.of(null, userUUID, email, password, roles, createdAt, updatedAt);
    }
}
