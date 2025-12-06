package kr.minimalest.api.domain.discovery.tag.repository;

import kr.minimalest.api.domain.discovery.tag.ArticleTag;
import kr.minimalest.api.domain.discovery.tag.ArticleTagId;
import kr.minimalest.api.domain.discovery.tag.TagId;
import kr.minimalest.api.domain.writing.ArticleId;

import java.util.List;
import java.util.Optional;

/**
 * ArticleTag 저장소 인터페이스
 */
public interface ArticleTagRepository {

    /**
     * 글과 태그의 관계 저장
     */
    ArticleTag save(ArticleTag articleTag);

    /**
     * ID로 관계 조회
     */
    Optional<ArticleTag> findById(ArticleTagId id);

    /**
     * 특정 글의 모든 태그 조회
     */
    List<ArticleTag> findByArticleId(ArticleId articleId);

    /**
     * 특정 태그가 붙은 모든 글 조회
     */
    List<ArticleTag> findByTagId(TagId tagId);

    /**
     * 특정 글의 특정 태그 조회 (중복 추가 방지용)
     */
    Optional<ArticleTag> findByArticleIdAndTagId(ArticleId articleId, TagId tagId);

    /**
     * 특정 글의 모든 태그 이름 조회
     */
    List<String> findTagNamesByArticleId(ArticleId articleId);

    /**
     * 관계 삭제
     */
    void delete(ArticleTag articleTag);

    /**
     * 특정 글의 모든 태그 삭제
     */
    void deleteByArticleId(ArticleId articleId);
}
