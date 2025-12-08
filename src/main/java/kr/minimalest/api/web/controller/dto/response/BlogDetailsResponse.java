package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.blog.BlogDetails;

import java.util.UUID;

public record BlogDetailsResponse(
        UUID blogId,
        AuthorResponse author,
        String about
) {
    public static BlogDetailsResponse of(BlogDetails blogDetails) {
        return new BlogDetailsResponse(
                blogDetails.blogId().id(),
                new AuthorResponse(
                        blogDetails.authorInfo().authorId(),
                        blogDetails.authorInfo().penName(),
                        blogDetails.authorInfo().profileImageUrl()
                ),
                blogDetails.about().value()
        );
    }
}
