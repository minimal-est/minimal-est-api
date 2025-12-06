package kr.minimalest.api.infrastructure.persistence.tag;

import kr.minimalest.api.domain.discovery.tag.Tag;
import kr.minimalest.api.domain.discovery.tag.TagId;
import kr.minimalest.api.domain.discovery.tag.TagName;
import kr.minimalest.api.domain.discovery.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Tag Repository 구현
 * Domain 인터페이스를 Spring Data JPA로 구현
 */
@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {

    private final SpringDataJpaTagRepository jpaRepository;

    @Override
    public Tag save(Tag tag) {
        return jpaRepository.save(tag);
    }

    @Override
    public Optional<Tag> findById(TagId tagId) {
        return jpaRepository.findById(tagId);
    }

    @Override
    public Optional<Tag> findByName(TagName tagName) {
        return jpaRepository.findByNameValue(tagName.value());
    }

    @Override
    public List<Tag> findByIds(List<TagId> tagIds) {
        return jpaRepository.findAllById(tagIds);
    }

    @Override
    public List<Tag> findByNames(List<TagName> tagNames) {
        List<String> names = tagNames.stream()
                .map(TagName::value)
                .toList();
        return jpaRepository.findByNameValueIn(names);
    }

    @Override
    public List<Tag> findAll() {
        return jpaRepository.findAll();
    }
}
