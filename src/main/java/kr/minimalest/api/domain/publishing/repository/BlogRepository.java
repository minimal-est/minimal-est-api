package kr.minimalest.api.domain.publishing.repository;

import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.access.UserId;

import java.util.List;
import java.util.Optional;

public interface BlogRepository {

    BlogId create(Blog blog);

    Optional<Blog> findById(BlogId blogId);

    Optional<Blog> findByUserId(UserId userId);

    List<Blog> findAllByIds(List<BlogId> blogIds);

    boolean hasBlogByUserId(UserId userId);

    boolean existsByPenName(PenName penName);
}
