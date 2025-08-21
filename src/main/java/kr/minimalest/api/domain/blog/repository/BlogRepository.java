package kr.minimalest.api.domain.blog.repository;

import kr.minimalest.api.domain.blog.Blog;
import kr.minimalest.api.domain.blog.BlogId;
import kr.minimalest.api.domain.blog.PenName;
import kr.minimalest.api.domain.user.UserId;

import java.util.Optional;

public interface BlogRepository {

    BlogId create(Blog blog);

    Optional<Blog> findById(BlogId blogId);

    Optional<Blog> findByUserId(UserId userId);

    boolean hasBlogByUserId(UserId userId);

    boolean existsByPenName(PenName penName);
}
