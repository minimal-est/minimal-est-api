package kr.minimalest.api.web;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.util.StringUtils;

public record Detail(@JsonValue String value) {
    public Detail {
        if (!StringUtils.hasText(value)) {
            value = "예외가 발생했습니다.";
        }
    }

    public static Detail of(String value) {
        return new Detail(value);
    }
}
