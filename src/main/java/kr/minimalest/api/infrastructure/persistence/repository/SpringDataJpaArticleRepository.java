package kr.minimalest.api.infrastructure.persistence.repository;

import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataJpaArticleRepository extends JpaRepository<Article, ArticleId> {

    @Query("SELECT a FROM Article a")
    List<Article> findTopN(Pageable pageable);

    @Query("SELECT a.id FROM  Article a")
    List<ArticleId> findTopNIds(Pageable pageable);

    Page<Article> findAllByStatusAndBlogId(ArticleStatus status, BlogId blogId, Pageable pageable);
}
