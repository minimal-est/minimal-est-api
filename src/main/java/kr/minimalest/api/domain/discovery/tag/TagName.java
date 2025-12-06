package kr.minimalest.api.domain.discovery.tag;

import org.springframework.util.Assert;

public record TagName(String value) {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 30;
    private static final String VALID_PATTERN = "^[a-zA-Z0-9가-힣]+$";

    public TagName {
        Assert.hasText(value, "태그 이름은 비어있을 수 없습니다.");
        validateLength(value);
        validateCharacters(value);
    }

    private static void validateLength(String value) {
        if (value.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("태그는 최소 " + MIN_LENGTH + "자 이상이어야 합니다.");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("태그는 최대 " + MAX_LENGTH + "자 이하여야 합니다.");
        }
    }

    private static void validateCharacters(String value) {
        if (!value.matches(VALID_PATTERN)) {
            throw new IllegalArgumentException("태그는 알파벳, 한글, 숫자만 포함할 수 있습니다.");
        }
    }

    public static TagName of(String value) {
        return new TagName(value);
    }
}
