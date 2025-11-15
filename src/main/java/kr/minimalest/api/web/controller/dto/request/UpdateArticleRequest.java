package kr.minimalest.api.web.controller.dto.request;

public record UpdateArticleRequest(
        String title,
        String content,
        String description
) {
    public static UpdateArticleRequest of(String title, String content, String description) {
        return new UpdateArticleRequest(title, content, description);
    }
}
