package kr.minimalest.api.infrastructure.persistence.repository;

import jakarta.persistence.EntityManager;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.repository.ArticleRepository;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.Password;
import kr.minimalest.api.domain.access.User;
import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.infrastructure.persistence.repository.adapter.ArticleRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
@Import(ArticleRepositoryAdapter.class)
class JpaArticleRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    ArticleRepository articleRepository;

    UserId savedUserId;

    BlogId savedBlogId;

    @BeforeEach
    @Transactional
    void setUp() {
        User user = User.signUp(Email.of("test@test.com"), Password.of("enc-test"));
        em.persist(user);
        savedUserId = user.getId();

        Blog blog = Blog.create(savedUserId, PenName.of("31n5ang"));
        em.persist(blog);
        savedBlogId = blog.getId();

        em.flush();
        em.clear();
    }

    @Nested
    @DisplayName("글 저장")
    class SaveArticle {

        @Test
        @DisplayName("글 저장 시 글 ID를 반환한다")
        @Transactional
        void shouldReturnArticleIdWhenArticleIsSaved() {
            // given
            Article article = Article.create(savedBlogId);

            // when
            ArticleId savedArticleId = articleRepository.save(article);

            // then
            assertThat(savedArticleId).isEqualTo(article.getId());
        }
    }
}
