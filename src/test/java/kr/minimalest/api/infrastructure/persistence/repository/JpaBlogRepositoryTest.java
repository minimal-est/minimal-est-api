package kr.minimalest.api.infrastructure.persistence.repository;

import jakarta.persistence.EntityManager;
import kr.minimalest.api.domain.blog.Blog;
import kr.minimalest.api.domain.blog.BlogId;
import kr.minimalest.api.domain.blog.PenName;
import kr.minimalest.api.domain.user.Email;
import kr.minimalest.api.domain.user.Password;
import kr.minimalest.api.domain.user.User;
import kr.minimalest.api.domain.user.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import(JpaBlogRepository.class)
class JpaBlogRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    JpaBlogRepository blogRepository;

    UserId savedUserId;

    @BeforeEach
    @Transactional
    void setUp() {
        User user = User.signUp(
                Email.of("test@test.com"),
                Password.of("enc-test1234")
        );
        em.persist(user);
        em.flush();
        em.clear();

        this.savedUserId = user.getId();
    }

    @Nested
    @DisplayName("블로그 생성")
    class CreateBlog {

        @Test
        @DisplayName("블로그 생성 후 블로그 ID를 반환한다")
        @Transactional
        void shouldReturnBlogIdWhenBlogIsCreated() {
            // given
            Blog blog = Blog.create(savedUserId, PenName.of("31n5ang"));

            // when
            BlogId savedBlogId = blogRepository.create(blog);

            // then
            assertThat(savedBlogId).isEqualTo(blog.getId());
        }
    }

    @Nested
    @DisplayName("블로그 조회")
    class FindBlog {

        @Test
        @DisplayName("사용자 ID로 블로그 존재 유무 True/False를 반환한다")
        @Transactional
        void shouldReturnTrueWhenBlogExistsByUserId() {
            // given
            Blog savedBlog = Blog.create(savedUserId, PenName.of("31n5ang"));
            em.persist(savedBlog);
            em.flush();
            em.clear();

            // when
            boolean hasBlog = blogRepository.hasBlogByUserId(savedUserId);
            boolean hasBlog2 = blogRepository.hasBlogByUserId(UserId.generate());

            // then
            assertThat(hasBlog).isTrue();
            assertThat(hasBlog2).isFalse();
        }

        @Test
        @DisplayName("블로그 ID로 블로그를 조회한다")
        @Transactional
        void shouldReturnBlogWhenFindBlogById() {
            // given
            Blog blog = Blog.create(savedUserId, PenName.of("31n5ang"));
            em.persist(blog);
            em.flush();
            em.clear();

            // when
            Optional<Blog> optionalBlog = blogRepository.findById(blog.getId());

            // then
            assertThat(optionalBlog).isPresent();
            assertThat(optionalBlog.get().getAuthor().getPenName().name()).isEqualTo("31n5ang");
        }
    }

    @Nested
    @DisplayName("작가 조회")
    class FindPenName {

        @Test
        @DisplayName("작가 펜네임이 이미 존재하면 True를 반환한다")
        void shouldReturnTrueWhenPenNameAlreadyExists() {
            // given
            Blog blog = Blog.create(savedUserId, PenName.of("already-exists"));
            em.persist(blog);
            em.flush();
            em.clear();

            // when
            boolean exists = blogRepository.existsByPenName(PenName.of("already-exists"));
            boolean exists2 = blogRepository.existsByPenName(PenName.of("no-exists"));

            // then
            assertThat(exists).isTrue();
            assertThat(exists2).isFalse();
        }
    }
}
