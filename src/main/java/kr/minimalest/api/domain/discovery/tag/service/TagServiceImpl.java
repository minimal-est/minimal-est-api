package kr.minimalest.api.domain.discovery.tag.service;

import kr.minimalest.api.domain.discovery.tag.Tag;
import kr.minimalest.api.domain.discovery.tag.TagId;
import kr.minimalest.api.domain.discovery.tag.TagName;
import kr.minimalest.api.domain.discovery.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> findOrCreateTags(List<String> tagNames) {
        // 유효하지 않은 태그 필터링
        List<String> validTagNames = tagNames.stream()
                .filter(this::isValidTagName)
                .distinct()
                .toList();

        // 각 태그를 생성 또는 조회
        return validTagNames.stream()
                .map(this::findOrCreateTag)
                .toList();
    }

    @Override
    public Tag findOrCreateTag(String tagName) {
        // 유효성 검사 (TagName 생성 시 자동으로 검사됨)
        TagName tag = TagName.of(tagName);

        // 기존 태그 조회
        Optional<Tag> existingTag = tagRepository.findByName(tag);
        if (existingTag.isPresent()) {
            return existingTag.get();
        }

        // 새 태그 생성 및 저장
        Tag newTag = Tag.of(tag.value());
        return tagRepository.save(newTag);
    }

    @Override
    @Transactional(readOnly = true)
    public Tag getTagById(TagId tagId) {
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException("태그를 찾을 수 없습니다: " + tagId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    /**
     * 태그 이름이 유효한지 확인합니다.
     * TagName 생성 시 자동으로 검사되므로, 여기서는 예외 발생을 캐치합니다.
     */
    private boolean isValidTagName(String tagName) {
        try {
            TagName.of(tagName);
            return true;
        } catch (IllegalArgumentException e) {
            // 유효하지 않은 태그는 무시
            return false;
        }
    }
}
