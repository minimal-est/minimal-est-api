package kr.minimalest.api.infrastructure.persistence.repository;

import jakarta.persistence.EntityManager;
import kr.minimalest.api.domain.article.Article;
import kr.minimalest.api.domain.article.ArticleId;
import kr.minimalest.api.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaArticleRepository implements ArticleRepository {
    
    private final EntityManager em;

    @Override
    public ArticleId save(Article article) {
        em.persist(article);
        return article.getId();
    }
}
