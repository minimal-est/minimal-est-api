package kr.minimalest.api.infrastructure.persistence.tag;

import kr.minimalest.api.domain.discovery.tag.ArticleTag;
import kr.minimalest.api.domain.discovery.tag.ArticleTagId;
import kr.minimalest.api.domain.discovery.tag.TagId;
import kr.minimalest.api.domain.writing.ArticleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA 기반 ArticleTag 저장소 (Spring Data용)
 */
public interface SpringDataJpaArticleTagRepository extends JpaRepository<ArticleTag, ArticleTagId> {

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
    @Query("SELECT t.name.value FROM ArticleTag at JOIN Tag t ON at.tagId = t.id WHERE at.articleId = :articleId")
    List<String> findTagNamesByArticleId(@Param("articleId") ArticleId articleId);

    /**
     * 특정 글의 모든 태그 삭제
     */
    void deleteByArticleId(ArticleId articleId);
}
