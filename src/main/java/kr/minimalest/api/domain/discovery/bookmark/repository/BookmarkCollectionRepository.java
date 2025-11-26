package kr.minimalest.api.domain.discovery.bookmark.repository;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollection;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;

import java.util.List;
import java.util.Optional;

public interface BookmarkCollectionRepository {

    /**
     * 컬렉션 저장
     */
    void save(BookmarkCollection collection);

    /**
     * 컬렉션 조회
     */
    Optional<BookmarkCollection> findById(BookmarkCollectionId id);

    /**
     * 사용자의 모든 컬렉션 조회
     */
    List<BookmarkCollection> findByUserId(UserId userId);

    /**
     * 사용자의 기본 컬렉션 조회
     */
    Optional<BookmarkCollection> findDefaultByUserId(UserId userId);

    /**
     * 컬렉션 삭제
     */
    void delete(BookmarkCollection collection);

    /**
     * 컬렉션 ID로 삭제
     */
    void deleteById(BookmarkCollectionId id);

    /**
     * 사용자의 모든 컬렉션 삭제 (사용자 탈퇴 시)
     */
    void deleteByUserId(UserId userId);
}
