package kr.minimalest.api.common.web;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.util.StringUtils;

public record Title(@JsonValue String value) {
    public Title {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("Title은 비어있을 수 없습니다!");
        }
    }

    public static Title of(String value) {
        return new Title(value);
    }
}
