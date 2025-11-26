package kr.minimalest.api.infrastructure.persistence.bookmark;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollection;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaBookmarkCollectionRepository extends JpaRepository<BookmarkCollection, BookmarkCollectionId> {

    /**
     * 사용자의 모든 컬렉션 조회
     */
    List<BookmarkCollection> findByUserIdOrderByCreatedAtDesc(UserId userId);

    /**
     * 사용자의 기본 컬렉션 조회
     */
    Optional<BookmarkCollection> findByUserIdAndIsDefaultTrue(UserId userId);

    /**
     * 사용자의 모든 컬렉션 삭제 (사용자 탈퇴 시)
     */
    void deleteByUserId(UserId userId);
}
