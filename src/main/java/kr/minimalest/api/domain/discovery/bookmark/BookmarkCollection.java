package kr.minimalest.api.domain.discovery.bookmark;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kr.minimalest.api.domain.AggregateRoot;
import kr.minimalest.api.domain.access.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 북마크 컬렉션 (폴더 개념)
 * 사용자가 저장한 글들을 카테고리로 정리
 */
@Getter
@Entity
@Table(name = "bookmark_collections")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BookmarkCollection extends AggregateRoot {

    @EmbeddedId
    private BookmarkCollectionId id;

    @Embedded
    @AttributeOverride(
        name = "id",
        column = @Column(name = "user_id", nullable = false)
    )
    private UserId userId;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 사용자가 만드는 컬렉션
     */
    public static BookmarkCollection create(UserId userId, String title, String description) {
        BookmarkCollection collection = new BookmarkCollection();
        collection.id = BookmarkCollectionId.generate();
        collection.userId = userId;
        collection.title = title;
        collection.description = description;
        collection.isPublic = false;
        collection.isDefault = false;
        collection.createdAt = LocalDateTime.now();
        collection.updatedAt = LocalDateTime.now();
        return collection;
    }

    /**
     * 기본 컬렉션 (사용자 가입 시 자동 생성)
     */
    public static BookmarkCollection createDefault(UserId userId) {
        BookmarkCollection collection = new BookmarkCollection();
        collection.id = BookmarkCollectionId.generate();
        collection.userId = userId;
        collection.title = "나중에 읽기";
        collection.description = null;
        collection.isPublic = false;
        collection.isDefault = true;
        collection.createdAt = LocalDateTime.now();
        collection.updatedAt = LocalDateTime.now();
        return collection;
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void togglePublic() {
        this.isPublic = !this.isPublic;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isOwnedBy(UserId userId) {
        if (userId == null) return false;
        return this.userId.equals(userId);
    }

}
