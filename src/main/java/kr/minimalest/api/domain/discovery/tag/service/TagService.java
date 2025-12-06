package kr.minimalest.api.domain.discovery.tag.service;

import kr.minimalest.api.domain.discovery.tag.Tag;
import kr.minimalest.api.domain.discovery.tag.TagId;

import java.util.List;

/**
 * Tag 서비스 인터페이스
 */
public interface TagService {

    /**
     * 여러 태그 이름으로 태그를 생성 또는 조회합니다.
     * 유효하지 않은 태그는 자동으로 필터링됩니다.
     *
     * @param tagNames 태그 이름 목록
     * @return 유효한 태그들 (새로 생성되었거나 기존 태그)
     */
    List<Tag> findOrCreateTags(List<String> tagNames);

    /**
     * 단일 태그를 생성 또는 조회합니다.
     *
     * @param tagName 태그 이름
     * @return 태그
     */
    Tag findOrCreateTag(String tagName);

    /**
     * ID로 태그 조회
     */
    Tag getTagById(TagId tagId);

    /**
     * 모든 태그 조회
     */
    List<Tag> getAllTags();
}
