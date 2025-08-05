package kr.minimalest.api.infrastructure.persistence.blog;

import jakarta.persistence.EntityManager;
import kr.minimalest.api.domain.blog.Blog;
import kr.minimalest.api.domain.blog.BlogId;
import kr.minimalest.api.domain.blog.BlogUUID;
import kr.minimalest.api.domain.blog.repository.BlogRepository;
import kr.minimalest.api.infrastructure.persistence.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JpaBlogRepository implements BlogRepository {

    private final EntityManager em;

    @Override
    public BlogId saveWithUserId(Blog blog) {
        UserEntity userEntity = em.find(UserEntity.class, blog.userId().value());
        BlogEntity blogEntity = BlogMapper.toEntity(blog, userEntity);
        em.persist(blogEntity);
        return BlogId.of(blogEntity.getId());
    }

    @Override
    public Optional<Blog> findById(BlogId blogId) {
        BlogEntity blogEntity = em.find(BlogEntity.class, blogId.value());
        return Optional.ofNullable(BlogMapper.toDomain(blogEntity));
    }

    @Override
    public Optional<Blog> findByUUID(BlogUUID blogUUID) {
        return em.createQuery("SELECT b FROM BlogEntity b WHERE blogUUID = :blogUUID", BlogEntity.class)
                .setParameter("blogUUID", blogUUID.value())
                .getResultStream()
                .findFirst()
                .map(BlogMapper::toDomain);
    }
}
