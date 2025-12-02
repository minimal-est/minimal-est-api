package kr.minimalest.api.domain.writing.service;

import kr.minimalest.api.domain.writing.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 아티클 검색 서비스 인터페이스
 * 향후 Elasticsearch로 대체 가능하도록 설계
 */
public interface ArticleSearchService {
    /**
     * 쿼리로 발행된 아티클 검색
     * @param query 검색 키워드
     * @param pageable 페이지 정보
     * @return 검색 결과 페이지
     */
    Page<Article> searchPublishedArticles(String query, Pageable pageable);
}
