package kr.minimalest.api.domain.user;

import org.springframework.util.StringUtils;

public record Email(String value) {

        public Email {
                if (!StringUtils.hasText(value)) {
                        throw new IllegalArgumentException("Email은 비어있을 수 없습니다!");
                }
        }

        public static Email of(String value) {
                return new Email(value);
        }
}
