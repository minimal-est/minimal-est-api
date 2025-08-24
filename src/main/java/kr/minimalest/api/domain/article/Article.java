package kr.minimalest.api.domain.article;

import jakarta.persistence.*;
import kr.minimalest.api.domain.AggregateRoot;
import kr.minimalest.api.domain.article.event.ArticleCompletedEvent;
import kr.minimalest.api.domain.article.event.ArticleUpdatedEvent;
import kr.minimalest.api.domain.article.event.ArticleCreatedEvent;
import kr.minimalest.api.domain.blog.BlogId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "articles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
            column = @Column(name = "title", nullable = false, length = 50)
    )
    private Title title;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    )
    private Content content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArticleStatus status = ArticleStatus.DRAFT;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    public static Article create(BlogId blogId) {
        Article article = new Article(
                ArticleId.generate(),
                blogId,
                Title.empty(),
                Content.empty(),
                ArticleStatus.DRAFT,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
        article.registerEvent(ArticleCreatedEvent.of(article.id, blogId));
        return article;
    }

    public void update(Title title, Content content) {
        if (status != ArticleStatus.DRAFT) {
            throw new IllegalStateException("작성 중인 상태일때만 수정 가능합니다.");
        }
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
        this.registerEvent(ArticleUpdatedEvent.of(id));
    }

    public void complete() {
        if (status != ArticleStatus.DRAFT) {
            throw new IllegalStateException("완료할 수 없는 상태입니다.");
        }
        this.title = validateTitle(title);
        this.content = validateContent(content);
        this.status = ArticleStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.registerEvent(ArticleCompletedEvent.of(id));
    }

    private static Title validateTitle(Title title) {
        Title trimmedTitle = title.trim();
        int trimmedLength = trimmedTitle.length();
        if (trimmedLength > 100) {
            throw new IllegalArgumentException("제목은 100자를 초과할 수 없습니다.");
        }
        if (trimmedLength == 0) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        return trimmedTitle;
    }

    private static Content validateContent(Content content) {
        int length = content.length();
        if (length > 30_000) {
            throw new IllegalArgumentException("본문은 3만자를 초과할 수 없습니다.");
        }
        if (length < 10) {
            throw new IllegalArgumentException("본문은 10자 이상이어야 합니다.");
        }
        return content;
    }
}
