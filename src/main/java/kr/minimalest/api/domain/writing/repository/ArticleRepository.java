package kr.minimalest.api.domain.writing.repository;

import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.writing.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    ArticleId save(Article article);

    Optional<Article> findById(ArticleId articleId);

    List<Article> findAllByIds(List<ArticleId> articleIds);

    List<ArticleId> findTopNIdsByOrderByPublishedAtDesc(int page, int limit);

    Page<Article> findAllDraftedByBlogId(BlogId blogId, Pageable pageable);

    Page<Article> findAllCompletedByBlogId(BlogId blogId, Pageable pageable);

    Page<Article> findAllMyArticles(BlogId blogId, ArticleStatus status, String searchKeyword, Pageable pageable);
}
