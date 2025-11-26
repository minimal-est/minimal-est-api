package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.blog.BlogInfo;

import java.util.UUID;

public record BlogInfoResponse(
        UUID blogId,
        UUID userId,
        AuthorResponse author
) {
    public static BlogInfoResponse of(BlogInfo blogInfo) {
        return new BlogInfoResponse(
                blogInfo.blogId().id(),
                blogInfo.userId().id(),
                new AuthorResponse(
                        blogInfo.author().getUserId().id(),
                        blogInfo.author().getPenName().value(),
                        blogInfo.author().getProfile().url()
                )
        );
    }
}
