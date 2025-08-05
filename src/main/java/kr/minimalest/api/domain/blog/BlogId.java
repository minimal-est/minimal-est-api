package kr.minimalest.api.domain.blog;

public record BlogId(Long value) {

    public BlogId {
        if (value < 0) {
            throw new IllegalStateException("BlogId는 음수가 될 수 없습니다!");
        }
    }

    public static BlogId of(Long value) {
        return new BlogId(value);
    }
}
