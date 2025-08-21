package kr.minimalest.api.domain.article;

public enum ArticleStatus {
    DRAFT("작성중"),
    COMPLETED("작성완료"),
    DELETED("삭제됨");

    private final String description;

    ArticleStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
