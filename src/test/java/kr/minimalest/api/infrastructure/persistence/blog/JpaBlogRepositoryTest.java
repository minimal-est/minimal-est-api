package kr.minimalest.api.infrastructure.persistence.blog;

import jakarta.persistence.EntityManager;
import kr.minimalest.api.domain.blog.*;
import kr.minimalest.api.domain.user.RoleType;
import kr.minimalest.api.domain.user.UserId;
import kr.minimalest.api.infrastructure.persistence.role.RoleEntity;
import kr.minimalest.api.infrastructure.persistence.user.UserEntity;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@Import(JpaBlogRepository.class)
class JpaBlogRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    JpaBlogRepository blogRepository;

    BlogFixture fixture;

    @BeforeEach
    void userSetup() {
        RoleEntity mockRoleEntity = new RoleEntity(null, RoleType.USER);
        em.persist(mockRoleEntity);

        UserEntity mockUserEntity = new UserEntity(
                null,
                UUID.randomUUID().toString(),
                "test@test.com",
                "password",
                Set.of(mockRoleEntity),
                null,
                null
        );
        em.persist(mockUserEntity);

        em.flush();
        em.clear();

        UserEntity persistedUser = em.find(UserEntity.class, mockUserEntity.getId());
        fixture = new BlogFixture(UserId.of(persistedUser.getId()));
    }

    @Getter
    private static class BlogFixture {
        private final BlogUUID blogUUID = BlogUUID.of(UUID.randomUUID().toString());
        private final Title title = Title.of("블로그 제목입니다.");
        private final Description description = Description.of("블로그 설명입니다.");
        private final UserId userId;

        public BlogFixture(UserId userId) {
            this.userId = userId;
        }

        public Blog getBlogWithoutId() {
            return Blog.withoutId(
                    this.blogUUID,
                    this.userId,
                    this.title,
                    this.description,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
        }
    }

    @Nested
    @DisplayName("블로그를 저장한다")
    class BlogSave {

        @Test
        @DisplayName("정상적으로 저장되면 BlogId가 반환된다")
        void shouldReturnBlogIdWhenSaved() {
            // given
            Blog blogToSave = fixture.getBlogWithoutId();

            // when
            BlogId savedBlogId = blogRepository.saveWithUserId(blogToSave);

            // then
            assertThat(savedBlogId).isNotNull();
            assertThat(savedBlogId.value()).isPositive();
        }
    }

    @Nested
    @DisplayName("블로그 조회")
    class FindBlog {

        @Test
        @DisplayName("ID로 블로그를 조회한다")
        void shouldFindBlogById() {
            // given
            BlogId savedBlogId = blogRepository.saveWithUserId(fixture.getBlogWithoutId());

            // when
            Optional<Blog> optionalBlog = blogRepository.findById(savedBlogId);

            // then
            assertThat(optionalBlog).isPresent();
            optionalBlog.ifPresent(blog -> {
                        assertThat(blog.blogId()).isEqualTo(savedBlogId);
                    }
            );
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회하면 빈 Optional을 반환한다")
        void shouldReturnEmptyWhenFindWithNonExistentId() {
            // given
            BlogId nonExistentId = BlogId.of(999L);

            // when
            Optional<Blog> optionalBlog = blogRepository.findById(nonExistentId);

            // then
            assertThat(optionalBlog).isEmpty();
        }

        @Test
        @DisplayName("UUID로 블로그를 조회한다.")
        void shouldFindBlogByUUID() {
            // given
            blogRepository.saveWithUserId(fixture.getBlogWithoutId());

            // when
            Optional<Blog> optionalBlog = blogRepository.findByUUID(fixture.getBlogUUID());

            // then
            assertThat(optionalBlog).isPresent();
            optionalBlog.ifPresent(blog -> {
                        assertThat(blog.blogUUID()).isEqualTo(fixture.blogUUID);
                    }
            );
        }
    }
}
