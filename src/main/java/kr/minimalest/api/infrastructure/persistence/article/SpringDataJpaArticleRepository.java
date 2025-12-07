package kr.minimalest.api.infrastructure.persistence.article;

import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.ArticleStatus;
import kr.minimalest.api.domain.writing.Slug;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataJpaArticleRepository extends JpaRepository<Article, ArticleId> {

    Optional<Article> findBySlug(Slug slug);

    @Query("SELECT a FROM Article a")
    List<Article> findTopN(Pageable pageable);

    @Query("SELECT a.id FROM Article a WHERE a.status = 'PUBLISHED'")
    List<ArticleId> findPublishedTopNIds(Pageable pageable);

    Page<Article> findAllByStatusAndBlogId(ArticleStatus status, BlogId blogId, Pageable pageable);

    List<Article> findAllByStatus(ArticleStatus status);

    Page<Article> findAllByBlogIdAndStatusNotOrderByUpdatedAtDesc(BlogId blogId, ArticleStatus status, Pageable pageable);

    @Query("SELECT a FROM Article a WHERE a.blogId = :blogId AND a.status != :status AND LOWER(a.title.value) LIKE LOWER(CONCAT('%', :titleKeyword, '%'))")
    Page<Article> findAllByBlogIdAndStatusNotAndTitleContainingIgnoreCaseOrderByUpdatedAtDesc(BlogId blogId, ArticleStatus status, String titleKeyword, Pageable pageable);

    /**
     * 해당 아티클 다음으로 발행된 아티클 반환
     * @param articleId 기준이될 아티클
     * @return 다음 발행된 아티클
     */
    @Query("""
        SELECT a
        FROM Article a
        JOIN Article b ON a.blogId = b.blogId
        WHERE b.id = :articleId
            AND a.status = 'PUBLISHED'
            AND a.publishedAt > b.publishedAt
        ORDER BY a.publishedAt
        LIMIT 1
    """)
    Optional<Article> findOneByIdPublishedAtAfter(ArticleId articleId);

    /**
     * 해당 아티클 이전으로 발행된 아티클 반환
     * @param articleId 기준이될 아티클
     * @return 이전 발행된 아티클
     */
    @Query("""
        SELECT a
        FROM Article a
        JOIN Article b ON a.blogId = b.blogId
        WHERE b.id = :articleId
            AND a.status = 'PUBLISHED'
            AND a.publishedAt < b.publishedAt
        ORDER BY a.publishedAt DESC
        LIMIT 1
    """)
    Optional<Article> findOneByIdPublishedAtBefore(ArticleId articleId);

    @Query(value =
        """
        SELECT * FROM articles a
        WHERE a.status = 'PUBLISHED'
        AND MATCH(a.title, a.content) AGAINST(:query IN NATURAL LANGUAGE MODE)
        ORDER BY MATCH(a.title, a.content) AGAINST(:query IN NATURAL LANGUAGE MODE) DESC
        """,
        countQuery =
        """
        SELECT COUNT(*) FROM articles a
        WHERE a.status = 'PUBLISHED'
        AND MATCH(a.title, a.content) AGAINST(:query IN NATURAL LANGUAGE MODE)
        """,
        nativeQuery = true
    )
    Page<Article> searchByTitleOrContent(String query, Pageable pageable);
}
