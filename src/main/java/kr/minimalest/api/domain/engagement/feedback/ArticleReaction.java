package kr.minimalest.api.domain.engagement.feedback;

import jakarta.persistence.*;
import kr.minimalest.api.domain.writing.ArticleId;
import kr.minimalest.api.domain.access.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "article_reactions",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_article_user_reaction",
                        columnNames = {"article_id", "reaction_user_id", "reaction_type"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleReaction {

    @EmbeddedId
    private ArticleReactionId id;

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "article_id", nullable = false)
    )
    private ArticleId articleId;

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "reaction_user_id", nullable = false)
    )
    private UserId userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false)
    private ReactionType reactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_state", nullable = false)
    private ReactionState reactionState;

    @Column(name = "reacted_at", nullable = true)
    private LocalDateTime reactedAt;

    public static ArticleReaction create(ArticleId articleId, UserId userId, ReactionType reactionType) {
        return new ArticleReaction(
                ArticleReactionId.generate(),
                articleId,
                userId,
                reactionType,
                ReactionState.NONE,
                null
        );
    }

    public ArticleReaction changeType(ReactionType reactionType) {
        this.reactionType = reactionType;
        return this;
    }
}
