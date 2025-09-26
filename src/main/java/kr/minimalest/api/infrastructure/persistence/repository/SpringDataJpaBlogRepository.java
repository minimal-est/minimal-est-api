package kr.minimalest.api.infrastructure.persistence.repository;

import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.BlogId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaBlogRepository extends JpaRepository<Blog, BlogId> {
}
