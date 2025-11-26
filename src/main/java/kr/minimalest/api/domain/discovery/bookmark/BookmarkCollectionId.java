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
public class BookmarkCollectionId {

    private UUID id;

    public static BookmarkCollectionId generate() {
        return new BookmarkCollectionId(UUID.randomUUID());
    }

    public static BookmarkCollectionId of(UUID id) {
        return new BookmarkCollectionId(id);
    }
}
