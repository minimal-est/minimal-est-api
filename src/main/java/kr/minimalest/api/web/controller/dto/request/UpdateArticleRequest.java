package kr.minimalest.api.web.controller.dto.request;

import java.util.List;

public record UpdateArticleRequest(
        String title,
        String content,
        String pureContent,
        String description,
        List<String> tags
) {
    public static UpdateArticleRequest of(String title, String content, String pureContent, String description, List<String> tags) {
        return new UpdateArticleRequest(title, content, pureContent, description, tags);
    }
}
