package kr.minimalest.api.domain.discovery.bookmark.repository;

import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.discovery.bookmark.Bookmark;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkId;
import kr.minimalest.api.domain.writing.ArticleId;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository {

    /**
     * 북마크 저장
     */
    void save(Bookmark bookmark);

    /**
     * 북마크 조회
     */
    Optional<Bookmark> findById(BookmarkId id);

    /**
     * 컬렉션의 모든 북마크 조회 (시퀀스 순)
     */
    List<Bookmark> findByCollectionIdOrderBySequence(BookmarkCollectionId collectionId);

    /**
     * 특정 글의 모든 북마크 조회
     */
    List<Bookmark> findByArticleId(ArticleId articleId);

    /**
     * 사용자와 글로 중복 확인 (어느 컬렉션이든)
     */
    boolean existsByUserIdAndArticleId(UserId userId, ArticleId articleId);

    /**
     * 사용자와 글로 기존 북마크 조회
     */
    Optional<Bookmark> findByUserIdAndArticleId(UserId userId, ArticleId articleId);

    /**
     * 컬렉션의 최대 시퀀스 조회
     */
    int findMaxSequenceByCollectionId(BookmarkCollectionId collectionId);

    /**
     * 북마크 삭제
     */
    void delete(Bookmark bookmark);

    /**
     * 북마크 ID로 삭제
     */
    void deleteById(BookmarkId id);

    /**
     * 컬렉션의 모든 북마크 삭제
     */
    void deleteByCollectionId(BookmarkCollectionId collectionId);

    /**
     * 글이 삭제될 때 해당 글의 모든 북마크 삭제
     */
    void deleteByArticleId(ArticleId articleId);

    /**
     * 컬렉션의 시퀀스 갭 채우기 (북마크 삭제 후)
     */
    void compactSequences(BookmarkCollectionId collectionId);

    /**
     * 컬렉션의 북마크 개수 조회
     */
    int countByCollectionId(BookmarkCollectionId collectionId);
}
