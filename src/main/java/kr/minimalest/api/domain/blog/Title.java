package kr.minimalest.api.domain.blog;

import org.springframework.util.StringUtils;

public record Title(String value) {

    public Title {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("Title이 비어있습니다!");
        }
    }

    public static Title of(String value) {
        return new Title(value);
    }
}
