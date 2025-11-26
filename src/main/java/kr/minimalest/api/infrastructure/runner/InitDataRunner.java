package kr.minimalest.api.infrastructure.runner;

import jakarta.persistence.EntityManager;
import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.Password;
import kr.minimalest.api.domain.access.User;
import kr.minimalest.api.domain.publishing.Blog;
import kr.minimalest.api.domain.publishing.PenName;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.Content;
import kr.minimalest.api.domain.writing.Title;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("default")
@RequiredArgsConstructor
@Slf4j
public class InitDataRunner implements CommandLineRunner {

    private final EntityManager em;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public void run(String... args) {
        // 이미 데이터가 있으면 스킵
        long userCount = em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
        if (userCount > 0) {
            log.info("초기 데이터가 이미 존재합니다");
            return;
        }

        User user = runInitUser();
        Blog blog = runInitBlog(user);
//        runInitArticle(blog, 1000);
    }

    public User runInitUser() {
        User user = User.signUp(
                Email.of("user@test.com"),
                Password.of(encoder.encode("user1234"))
        );

        User admin = User.signUp(
                Email.of("admin@test.com"),
                Password.of(encoder.encode("admin1234"))
        );

        em.persist(user);
        em.persist(admin);

        admin.assignAdmin();

        log.info("초기 데이터 삽입 완료: Roles, Admin User, Normal User");

        return user;
    }

    public Blog runInitBlog(User user) {
        Blog blog = Blog.create(user.getId(), PenName.of("송작가"));

        em.persist(blog);

        log.info("블로그 생성 완료: 송작가({}) ", user.getId().id());

        return blog;
    }

//    public void runInitArticle(Blog blog, int size) {
//        List<Article> articles = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//            Article article = Article.create(blog.getId());
//            articles.add(article);
//        }
//
//        int i = 0;
//        for (Article article : articles) {
//            article.update(Title.of("title" + i), Content.of("content content" + i));
//            article.complete();
//            em.persist(article);
//            i++;
//        }

//        Article article = Article.create(blog.getId());
//        article.update(
//                Title.of("첫 번째 글 - 미니멀리즘이란?"),
//                Content.of("불필요한 것을 줄이고 본질에 집중하는 미니멀리즘. 일상과 개발 모두에 적용할 수 있는 방법을 간단히 정리해봤습니다.")
//        );
//
//        article.complete();
//
//        em.persist(article);
//
//        log.info("글 저장 완료: {}개", size + 1);
//    }
}
