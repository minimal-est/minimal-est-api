package kr.minimalest.api.domain.engagement.comment;

import jakarta.persistence.*;
import kr.minimalest.api.domain.access.UserId;
import kr.minimalest.api.domain.writing.ArticleId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment {

    @EmbeddedId
    private CommentId id;

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "article_id", nullable = false)
    )
    private ArticleId articleId;

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "parent_id")
    )
    private CommentId parentId;  // NULL이면 댓글, 값이 있으면 대댓글

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "user_id")
    )
    private UserId userId;  // NULL이면 비회원

    @Column(name = "author_name", nullable = false, length = 50)
    private String authorName;

    @Column(name = "is_anonymous")
    private boolean isAnonymous;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    )
    private CommentContent content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommentStatus status;

    @Column(name = "like_count")
    private int likeCount = 0;

    @Column(name = "guest_password")
    private String guestPassword;  // 비회원 댓글 삭제용 비밀번호 (회원은 NULL)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // 댓글인지 대댓글인지 확인
    public boolean isReply() {
        return parentId != null;
    }

    // 댓글 삭제 (soft delete)
    public void delete() {
        this.status = CommentStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
    }

    // 댓글 생성 - 회원
    public static Comment createByMember(
            ArticleId articleId,
            CommentId parentId,
            UserId userId,
            String penName,
            boolean isAnonymous,
            String content
    ) {
        Comment comment = new Comment();
        comment.id = CommentId.generate();
        comment.articleId = articleId;
        comment.parentId = parentId;
        comment.userId = userId;
        comment.authorName = isAnonymous ? "(익명)" : penName;
        comment.isAnonymous = isAnonymous;
        comment.content = new CommentContent(content);
        comment.status = CommentStatus.ACTIVE;
        comment.likeCount = 0;
        comment.createdAt = LocalDateTime.now();
        return comment;
    }

    // 댓글 생성 - 비회원
    public static Comment createByGuest(
            ArticleId articleId,
            CommentId parentId,
            String guestName,
            String content,
            String guestPassword  // 비밀번호 (해시된 상태)
    ) {
        Comment comment = new Comment();
        comment.id = CommentId.generate();
        comment.articleId = articleId;
        comment.parentId = parentId;
        comment.userId = null;
        comment.authorName = guestName;
        comment.isAnonymous = false;
        comment.content = new CommentContent(content);
        comment.status = CommentStatus.ACTIVE;
        comment.likeCount = 0;
        comment.guestPassword = guestPassword;
        comment.createdAt = LocalDateTime.now();
        return comment;
    }
}
