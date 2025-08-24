package kr.minimalest.api.domain.article.repository;

import kr.minimalest.api.domain.article.Article;
import kr.minimalest.api.domain.article.ArticleId;

import java.util.Optional;

public interface ArticleRepository {

    ArticleId save(Article article);

    Optional<Article> findById(ArticleId articleId);
}
