package kr.minimalest.api.domain.discovery.tag;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Tag {

    @EmbeddedId
    private TagId id;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "name", nullable = false, unique = true)
    )
    private TagName name;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static Tag of(String name) {
        return new Tag(
                TagId.generate(),
                TagName.of(name),
                null
        );
    }
}
