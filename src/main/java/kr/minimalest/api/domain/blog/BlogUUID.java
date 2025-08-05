package kr.minimalest.api.domain.blog;

import org.springframework.util.StringUtils;

import java.util.UUID;

public record BlogUUID(String value) {
    public BlogUUID {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("UUID의 값이 존재하지 않습니다!");
        }

        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 UUID 형식입니다!", e);
        }
    }

    public static BlogUUID of(String value) {
        return new BlogUUID(value);
    }
}
