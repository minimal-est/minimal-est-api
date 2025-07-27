package kr.minimalest.api.domain.user;

import org.springframework.util.StringUtils;

public record Password(String value) {
        public Password {
                if (!StringUtils.hasText(value)) {
                        throw new IllegalArgumentException("Password는 비어있을 수 없습니다!");
                }
        }

        public static Password of(String value) {
                return new Password(value);
        }
}
