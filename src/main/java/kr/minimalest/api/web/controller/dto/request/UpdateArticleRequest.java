package kr.minimalest.api.web.controller.dto.request;

public record UpdateArticleRequest(

        String title,
        String content
) {
    public static UpdateArticleRequest of(String title, String content) {
        return new UpdateArticleRequest(title, content);
    }
}
