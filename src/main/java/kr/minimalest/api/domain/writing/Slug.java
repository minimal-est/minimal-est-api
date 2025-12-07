package kr.minimalest.api.domain.writing;

import java.util.UUID;

public record Slug(String value) {

    private static final int MAX_SLUG_LENGTH = 150;
    private static final int UUID_SUFFIX_LENGTH = 9; // "-" + 8자

    public Slug {
        if (value.length() > MAX_SLUG_LENGTH) {
            throw new IllegalArgumentException("Slug는 " + MAX_SLUG_LENGTH + "자를 초과할 수 없습니다.");
        }
    }

    public static Slug of(String title, UUID articleId) {
        String convertedTitle = convertToSlug(title);
        String shortId = articleId.toString().substring(0, 8);

        // 제목이 너무 길면 잘라내기
        int maxTitleLength = MAX_SLUG_LENGTH - UUID_SUFFIX_LENGTH;
        if (convertedTitle.length() > maxTitleLength) {
            convertedTitle = convertedTitle.substring(0, maxTitleLength);
            // 마지막 하이푼 제거
            convertedTitle = convertedTitle.replaceAll("-+$", "");
        }

        String slug = convertedTitle.isEmpty() ? shortId : convertedTitle + "-" + shortId;
        return new Slug(slug);
    }

    public static Slug empty() {
        return new Slug("-");
    }

    private static String convertToSlug(String title) {
        return title
            .toLowerCase()                           // 소문자로 변환
            .replaceAll("[^\\p{L}\\p{N}\\s]", "")   // 문자, 숫자, 공백만 유지 (특수문자 제거)
            .replaceAll("\\s+", "-")                 // 공백을 -로
            .replaceAll("-+", "-")                   // 연속 -를 하나로
            .replaceAll("^-|-$", "");                // 앞뒤 - 제거
    }
}
