package kr.minimalest.api.domain.writing;

import jakarta.persistence.*;
import kr.minimalest.api.domain.AggregateRoot;
import kr.minimalest.api.domain.publishing.BlogId;
import kr.minimalest.api.domain.writing.event.ArticleCompletedEvent;
import kr.minimalest.api.domain.writing.event.ArticleCreatedEvent;
import kr.minimalest.api.domain.writing.event.ArticleUpdatedEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "articles")
public class Article extends AggregateRoot {

    @EmbeddedId
    private ArticleId id;

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "blog_id", nullable = false)
    )
    private BlogId blogId;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "title", columnDefinition = "TEXT", nullable = false, length = 50)
    )
    private Title title;

    /**
     * JSON 형식
     */
    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "content", columnDefinition = "TEXT", nullable = true)
    )
    private Content content;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "pure_content", columnDefinition = "TEXT", nullable = true)
    )
    private Content pureContent;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    )
    private Description description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleStatus status = ArticleStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Visibility visibility = Visibility.PRIVATE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 마지막 수정 시간

    @Column(nullable = true)
    private LocalDateTime publishedAt; // 발행 시간

    @Column(nullable = true)
    private LocalDateTime deletedAt; // 삭제 시간

    public UUID getRawId() {
        return id.id();
    }

    public boolean isOwnedBy(BlogId blogId) {
        if (blogId == null) return false;
        return this.blogId.equals(blogId);
    }

    public static Article create(BlogId blogId) {
        Article article = new Article(
                ArticleId.generate(),
                blogId,
                Title.empty(),
                Content.empty(),
                Content.empty(),
                Description.empty(),
                ArticleStatus.DRAFT,
                Visibility.PRIVATE,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null
        );
        article.registerEvent(ArticleCreatedEvent.of(article.id, blogId));
        return article;
    }

    // 저장 (상태 유지)
    public void update(Title title, Content content, Content pureContent, Description description) {
        if (status == ArticleStatus.DELETED) {
            throw new IllegalStateException("삭제된 글을 수정할 수 없습니다.");
        }
        // 발행된 상태에서 수정 시, 유효성 검증
        if (status == ArticleStatus.PUBLISHED) {
            validateForPublish();
        }
        this.title = title;
        this.content = content;
        this.pureContent = pureContent;
        this.description = description;
        this.updatedAt = LocalDateTime.now();
        this.registerEvent(ArticleUpdatedEvent.of(id));
    }

    // 발행 (DRAFTED -> PUBLISHED)
    public void publish() {
        if (status != ArticleStatus.DRAFT) {
            throw new IllegalStateException("발행할 수 없는 상태입니다.");
        }
        validateForPublish();
        this.status = ArticleStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.registerEvent(ArticleCompletedEvent.of(id));
    }

    public void delete() {
        if (status == ArticleStatus.DELETED) {
            throw new IllegalStateException("이미 삭제된 글입니다.");
        }
        this.status = ArticleStatus.DELETED;
        this.updatedAt = LocalDateTime.now();
    }

    private void validateForPublish() {
        validateContent();
        validateAndTrimTitle();
        validateAndTrimDescription();
    }

    private void validateAndTrimTitle() {
        title = title.trim();
        int trimmedLength = title.length();
        if (trimmedLength > 100) {
            throw new IllegalArgumentException("제목은 100자를 초과할 수 없습니다.");
        }
        if (trimmedLength == 0) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
    }

    private void validateContent() {
        int length = pureContent.length();
        log.info("{}", length);
        if (length > 30_000) {
            throw new IllegalArgumentException("본문은 3만자를 초과할 수 없습니다.");
        }
        if (length < 10) {
            throw new IllegalArgumentException("본문은 10자 이상이어야 합니다.");
        }
    }

    private void validateAndTrimDescription() {
        description = description.trim();
        int length = description.length();
        if (length > 200) {
            throw new IllegalArgumentException("설명은 200자를 초과할 수 없습니다.");
        }
    }
}
