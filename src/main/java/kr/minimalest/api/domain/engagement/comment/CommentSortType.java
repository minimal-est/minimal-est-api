package kr.minimalest.api.domain.engagement.comment;

public enum CommentSortType {
    LATEST("최신순"),
    POPULAR("인기순");

    private final String displayName;

    CommentSortType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
