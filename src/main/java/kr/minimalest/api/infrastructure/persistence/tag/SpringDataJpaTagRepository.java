package kr.minimalest.api.infrastructure.persistence.tag;

import kr.minimalest.api.domain.discovery.tag.Tag;
import kr.minimalest.api.domain.discovery.tag.TagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA 기반 Tag 저장소 (Spring Data용)
 */
public interface SpringDataJpaTagRepository extends JpaRepository<Tag, TagId> {

    /**
     * 이름으로 태그 조회
     */
    Optional<Tag> findByNameValue(String name);

    /**
     * 여러 이름으로 태그 조회
     */
    List<Tag> findByNameValueIn(List<String> names);
}
