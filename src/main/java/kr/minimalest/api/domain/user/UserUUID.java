package kr.minimalest.api.domain.user;

import org.springframework.util.StringUtils;

import java.util.UUID;

public record UserUUID (String value) {

    public UserUUID {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("UUID의 값이 존재하지 않습니다!");
        }

        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 UUID 형식입니다!", e);
        }
    }

    public static UserUUID of(String value) {
        return new UserUUID(value);
    }
}
