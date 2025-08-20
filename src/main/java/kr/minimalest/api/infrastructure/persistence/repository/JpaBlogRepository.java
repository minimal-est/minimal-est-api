package kr.minimalest.api.infrastructure.persistence.repository;

import jakarta.persistence.EntityManager;
import kr.minimalest.api.domain.blog.Blog;
import kr.minimalest.api.domain.blog.BlogId;
import kr.minimalest.api.domain.blog.PenName;
import kr.minimalest.api.domain.blog.repository.BlogRepository;
import kr.minimalest.api.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaBlogRepository implements BlogRepository {

    private final EntityManager em;

    @Override
    public BlogId create(Blog blog) {
        em.persist(blog);
        return blog.getId();
    }

    @Override
    public Optional<Blog> findById(BlogId blogId) {
        Blog blog = em.find(Blog.class, blogId);
        return Optional.ofNullable(blog);
    }

    @Override
    public boolean hasBlogByUserId(UserId userId) {
        return em
                .createQuery(
                        "SELECT exists(SELECT 1 FROM Blog b WHERE b.author.userId = :userId)", Boolean.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    @Override
    public boolean existsByPenName(PenName penName) {
        return em.createQuery(
                "SELECT exists(SELECT 1 FROM Author a WHERE a.penName = :penName)", Boolean.class)
                .setParameter("penName", penName)
                .getSingleResult();
    }
}
