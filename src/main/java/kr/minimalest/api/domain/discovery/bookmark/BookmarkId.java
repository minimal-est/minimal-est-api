package kr.minimalest.api.domain.discovery.bookmark;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class BookmarkId {

    private UUID id;

    public static BookmarkId generate() {
        return new BookmarkId(UUID.randomUUID());
    }

    public static BookmarkId of(UUID id) {
        return new BookmarkId(id);
    }
}
