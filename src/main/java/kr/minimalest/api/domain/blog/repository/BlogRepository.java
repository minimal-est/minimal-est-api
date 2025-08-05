package kr.minimalest.api.domain.blog.repository;

import kr.minimalest.api.domain.blog.Blog;
import kr.minimalest.api.domain.blog.BlogId;
import kr.minimalest.api.domain.blog.BlogUUID;

import java.util.Optional;

public interface BlogRepository {

    BlogId saveWithUserId(Blog blog);

    Optional<Blog> findById(BlogId blogId);

    Optional<Blog> findByUUID(BlogUUID blogUUID);
}
