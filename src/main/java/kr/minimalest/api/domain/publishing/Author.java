package kr.minimalest.api.domain.publishing;

import jakarta.persistence.*;
import kr.minimalest.api.domain.access.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "authors")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Author {

    @EmbeddedId
    private AuthorId id;

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "owner_id", nullable = false, unique = true)
    )
    private UserId userId;

    @Embedded
    private PenName penName;

    static Author create(UserId userId, PenName penName) {
        Author author = new Author();
        author.id = AuthorId.generate();
        author.userId = userId;
        author.penName = penName;
        return author;
    }
}
