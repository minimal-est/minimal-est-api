package kr.minimalest.api.domain.discovery.tag.repository;

import kr.minimalest.api.domain.discovery.tag.Tag;
import kr.minimalest.api.domain.discovery.tag.TagId;
import kr.minimalest.api.domain.discovery.tag.TagName;

import java.util.List;
import java.util.Optional;

/**
 * Tag 저장소 인터페이스
 */
public interface TagRepository {

    /**
     * 태그 저장 (생성 또는 업데이트)
     */
    Tag save(Tag tag);

    /**
     * ID로 태그 조회
     */
    Optional<Tag> findById(TagId tagId);

    /**
     * 이름으로 태그 조회
     */
    Optional<Tag> findByName(TagName tagName);

    /**
     * 여러 ID로 태그 조회
     */
    List<Tag> findByIds(List<TagId> tagIds);

    /**
     * 여러 이름으로 태그 조회
     */
    List<Tag> findByNames(List<TagName> tagNames);

    /**
     * 모든 태그 조회
     */
    List<Tag> findAll();
}
