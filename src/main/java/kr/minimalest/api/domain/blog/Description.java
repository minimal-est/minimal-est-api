package kr.minimalest.api.domain.blog;

import org.springframework.util.StringUtils;

public record Description(String value) {

    public Description {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException("Description이 비어있습니다!");
        }
    }

    public static Description of(String value) {
        return new Description(value);
    }
}
