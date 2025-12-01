package kr.minimalest.api.web.controller.dto.request;

public record UpdateArticleRequest(
        String title,
        String content,
        String pureContent,
        String description
) {
    public static UpdateArticleRequest of(String title, String content, String pureContent, String description) {
        return new UpdateArticleRequest(title, content, pureContent, description);
    }
}
