package kr.minimalest.api.domain.engagement.feedback;

public enum ReactionType {
    READ("잘 읽었어요"),
    AGREE("공감해요"),
    USEFUL("유익해요");

    private final String label;

    ReactionType(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
