package kr.minimalest.api.domain.engagement.comment;

import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public record CommentId(UUID id) {
    public static CommentId generate() {
        return new CommentId(UUID.randomUUID());
    }

    public static CommentId of(UUID id) {
        return new CommentId(id);
    }
}
