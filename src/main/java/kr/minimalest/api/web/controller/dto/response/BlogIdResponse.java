package kr.minimalest.api.web.controller.dto.response;

import java.util.UUID;

public record BlogIdResponse(UUID blogId) {
    public static BlogIdResponse of(UUID blogId) {
        return new BlogIdResponse(blogId);
    }
}
