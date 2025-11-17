package kr.minimalest.api.domain.publishing;

import jakarta.persistence.*;
import kr.minimalest.api.domain.AggregateRoot;
import kr.minimalest.api.domain.publishing.event.CreatedBlogEvent;
import kr.minimalest.api.domain.access.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "blogs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Blog extends AggregateRoot {

    @EmbeddedId
    private BlogId id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    private LocalDateTime createdAt;

    public PenName getPenName() {
        return author.getPenName();
    }

    public UserId getOwnerUserId() {
        return author.getUserId();
    }

    public static Blog create(UserId ownerId, PenName authorPenName) {
        Blog blog = new Blog();
        blog.id = BlogId.generate();
        blog.author = Author.create(ownerId, authorPenName);
        blog.createdAt = LocalDateTime.now();
        blog.registerEvent(CreatedBlogEvent.of(blog.id, blog.author.getPenName()));
        return blog;
    }

    public boolean isOwnedBy(UserId userId) {
        if (userId == null) return false;
        return author.getUserId().equals(userId);
    }
}
