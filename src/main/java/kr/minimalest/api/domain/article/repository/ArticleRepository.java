package kr.minimalest.api.domain.article.repository;

import kr.minimalest.api.domain.article.Article;
import kr.minimalest.api.domain.article.ArticleId;

public interface ArticleRepository {

    ArticleId save(Article article);
}
