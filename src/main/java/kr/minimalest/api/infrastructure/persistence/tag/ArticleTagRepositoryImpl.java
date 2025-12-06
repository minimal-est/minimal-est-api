package kr.minimalest.api.infrastructure.persistence.tag;

import kr.minimalest.api.domain.discovery.tag.ArticleTag;
import kr.minimalest.api.domain.discovery.tag.ArticleTagId;
import kr.minimalest.api.domain.discovery.tag.TagId;
import kr.minimalest.api.domain.discovery.tag.repository.ArticleTagRepository;
import kr.minimalest.api.domain.writing.ArticleId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * ArticleTag Repository 구현
 * Domain 인터페이스를 Spring Data JPA로 구현
 */
@Repository
@RequiredArgsConstructor
public class ArticleTagRepositoryImpl implements ArticleTagRepository {

    private final SpringDataJpaArticleTagRepository jpaRepository;

    @Override
    public ArticleTag save(ArticleTag articleTag) {
        return jpaRepository.save(articleTag);
    }

    @Override
    public Optional<ArticleTag> findById(ArticleTagId id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<ArticleTag> findByArticleId(ArticleId articleId) {
        return jpaRepository.findByArticleId(articleId);
    }

    @Override
    public List<ArticleTag> findByTagId(TagId tagId) {
        return jpaRepository.findByTagId(tagId);
    }

    @Override
    public Optional<ArticleTag> findByArticleIdAndTagId(ArticleId articleId, TagId tagId) {
        return jpaRepository.findByArticleIdAndTagId(articleId, tagId);
    }

    @Override
    public List<String> findTagNamesByArticleId(ArticleId articleId) {
        return jpaRepository.findTagNamesByArticleId(articleId);
    }

    @Override
    public void delete(ArticleTag articleTag) {
        jpaRepository.delete(articleTag);
    }

    @Override
    public void deleteByArticleId(ArticleId articleId) {
        jpaRepository.deleteByArticleId(articleId);
    }
}
