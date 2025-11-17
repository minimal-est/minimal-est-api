package kr.minimalest.api.web.controller.dto.response;

import java.util.UUID;

public record ArticleIdResponse(UUID articleId) {
    public static ArticleIdResponse of(UUID articleId) {
        return new ArticleIdResponse(articleId);
    }
}
