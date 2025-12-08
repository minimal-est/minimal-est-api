package kr.minimalest.api.application.blog;

import kr.minimalest.api.application.article.AuthorInfo;
import kr.minimalest.api.domain.publishing.About;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.BlogId;

public record BlogDetails(
        BlogId blogId,
        AuthorInfo authorInfo,
        About about
) {
    public static BlogDetails of(Blog blog) {
        return new BlogDetails(
                blog.getId(),
                new AuthorInfo(
                        blog.getAuthor().getId().id(),
                        blog.getPenName().value(),
                        blog.getProfileImageUrl()
                ),
                blog.getAbout()
        );
    }
}
