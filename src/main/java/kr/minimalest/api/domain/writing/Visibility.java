package kr.minimalest.api.domain.writing;

public enum Visibility {
    PUBLIC("공개"),
    PRIVATE("비공개"),
    UNLISTED("링크공유");

    private final String description;

    Visibility(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
