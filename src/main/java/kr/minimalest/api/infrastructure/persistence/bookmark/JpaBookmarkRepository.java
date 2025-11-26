package kr.minimalest.api.infrastructure.persistence.bookmark;

import kr.minimalest.api.domain.discovery.bookmark.Bookmark;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkCollectionId;
import kr.minimalest.api.domain.discovery.bookmark.BookmarkId;
import kr.minimalest.api.domain.writing.ArticleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaBookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {

    /**
     * 컬렉션의 모든 북마크 조회 (시퀀스 순)
     */
    List<Bookmark> findByCollectionIdOrderBySequenceAsc(BookmarkCollectionId collectionId);

    /**
     * 특정 글의 모든 북마크 조회
     */
    List<Bookmark> findByArticleId(ArticleId articleId);

    /**
     * 컬렉션의 최대 시퀀스 조회
     */
    @Query("SELECT COALESCE(MAX(b.sequence), 0) FROM Bookmark b WHERE b.collectionId = :collectionId")
    int findMaxSequenceByCollectionId(@Param("collectionId") BookmarkCollectionId collectionId);

    /**
     * 컬렉션의 모든 북마크 삭제
     */
    @Modifying
    @Transactional
    void deleteByCollectionId(BookmarkCollectionId collectionId);

    /**
     * 글이 삭제될 때 해당 글의 모든 북마크 삭제
     */
    @Modifying
    @Transactional
    void deleteByArticleId(ArticleId articleId);

    /**
     * 컬렉션의 북마크 개수 조회
     */
    @Query("SELECT COUNT(b) FROM Bookmark b WHERE b.collectionId = :collectionId")
    int countByCollectionId(@Param("collectionId") BookmarkCollectionId collectionId);
}
