package kr.minimalest.api.application.article;

public record FindArticleBySlugArgument(
        String penName,
        String slug
) {
    public static FindArticleBySlugArgument of(String penName, String slug) {
        return new FindArticleBySlugArgument(penName, slug);
    }
}
