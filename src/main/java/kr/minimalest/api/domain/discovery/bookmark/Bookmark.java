package kr.minimalest.api.domain.discovery.bookmark;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import kr.minimalest.api.domain.writing.ArticleId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 북마크 (저장된 글)
 * 사용자가 컬렉션에 저장한 글
 */
@Getter
@Entity
@Table(name = "bookmarks", uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_bookmark_collection_article",
        columnNames = {"collection_id", "article_id"}
    )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Bookmark {

    @EmbeddedId
    private BookmarkId id;

    @Embedded
    @AttributeOverride(
        name = "id",
        column = @Column(name = "collection_id", nullable = false)
    )
    private BookmarkCollectionId collectionId;

    @Embedded
    @AttributeOverride(
        name = "id",
        column = @Column(name = "article_id", nullable = false)
    )
    private ArticleId articleId;

    @Column(name = "sequence", nullable = false)
    private int sequence;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 북마크 생성 (시퀀스는 추후 업데이트)
     */
    public static Bookmark create(BookmarkCollectionId collectionId, ArticleId articleId, int sequence) {
        Bookmark bookmark = new Bookmark();
        bookmark.id = BookmarkId.generate();
        bookmark.collectionId = collectionId;
        bookmark.articleId = articleId;
        bookmark.sequence = sequence;
        bookmark.createdAt = LocalDateTime.now();
        return bookmark;
    }

    /**
     * 북마크를 다른 컬렉션으로 이동
     */
    public void moveToCollection(BookmarkCollectionId newCollectionId) {
        this.collectionId = newCollectionId;
    }

    /**
     * 시퀀스 업데이트 (드래그 정렬 시)
     */
    public void updateSequence(int newSequence) {
        this.sequence = newSequence;
    }
}
