package kr.minimalest.api.infrastructure.persistence.article;

import kr.minimalest.api.domain.writing.Article;
import kr.minimalest.api.domain.writing.ArticleStatus;
import kr.minimalest.api.domain.writing.service.ArticleSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * JPA 기반 아티클 검색 구현
 * 향후 Elasticsearch로 대체 고려하기
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JpaArticleSearchService implements ArticleSearchService {

    private final SpringDataJpaArticleRepository articleRepository;

    @Override
    public Page<Article> searchPublishedArticles(String query, Pageable pageable) {
        Page<Article> result = articleRepository.searchByTitleOrContent(query, pageable);
        return result;
    }
}
