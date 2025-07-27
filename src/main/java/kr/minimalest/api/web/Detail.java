package kr.minimalest.api.web;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.util.StringUtils;

public record Detail(@JsonValue String value) {
    public Detail {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("Detail은 비어있을 수 없습니다!");
        }
    }

    public static Detail of(String value) {
        return new Detail(value);
    }
}
