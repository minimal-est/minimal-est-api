package kr.minimalest.api.domain.writing;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Content {

    private final String value;

    public Content(String value) {
        this.value = value;
    }

    public static Content of(String value) {
        return new Content(value);
    }

    public static Content empty() {
        return new Content("");
    }

    public int length() {
        return value.length();
    }

    public String getValue() {
        return value;
    }

    public String value() {
        return value;
    }

    public String extractPlainText() {
        if (value == null || value.isEmpty()) {
            return "";
        }
        // JSON에서 텍스트 노드만 추출 (간단한 구현)
        return value.replaceAll("<[^>]*>", "").trim();
    }
}
