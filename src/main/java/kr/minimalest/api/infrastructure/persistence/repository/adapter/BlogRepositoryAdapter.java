package kr.minimalest.api.infrastructure.persistence.repository.adapter;

import jakarta.persistence.EntityManager;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.publishing.repository.BlogRepository;
import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.infrastructure.persistence.repository.SpringDataJpaBlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BlogRepositoryAdapter implements BlogRepository {

    private final EntityManager em;
    private final SpringDataJpaBlogRepository springDataJpaBlogRepository;

    @Override
    @Transactional
    public BlogId create(Blog blog) {
        em.persist(blog);
        return blog.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Blog> findById(BlogId blogId) {
        Blog blog = em.find(Blog.class, blogId);
        return Optional.ofNullable(blog);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Blog> findByUserId(UserId userId) {
        return em.createQuery("SELECT b FROM Blog b JOIN FETCH b.author a WHERE b.author.userId = :userId", Blog.class)
                .setParameter("userId", userId)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<Blog> findAllByIds(List<BlogId> blogIds) {
        return springDataJpaBlogRepository.findAllById(blogIds);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasBlogByUserId(UserId userId) {
        return em
                .createQuery(
                        "SELECT exists(SELECT 1 FROM Blog b WHERE b.author.userId = :userId)", Boolean.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPenName(PenName penName) {
        return em.createQuery(
                "SELECT exists(SELECT 1 FROM Author a WHERE a.penName = :penName)", Boolean.class)
                .setParameter("penName", penName)
                .getSingleResult();
    }
}
