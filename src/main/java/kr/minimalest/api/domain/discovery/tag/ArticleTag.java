package kr.minimalest.api.domain.discovery.tag;

import jakarta.persistence.*;
import kr.minimalest.api.domain.writing.ArticleId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "article_tags", uniqueConstraints = @UniqueConstraint(columnNames = {"article_id", "tag_id"}))
public class ArticleTag {

    @EmbeddedId
    private ArticleTagId id;

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "article_id", nullable = false)
    )
    private ArticleId articleId;

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "tag_id", nullable = false)
    )
    private TagId tagId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static ArticleTag of(ArticleId articleId, TagId tagId) {
        return new ArticleTag(
                ArticleTagId.generate(),
                articleId,
                tagId,
                LocalDateTime.now()
        );
    }
}
