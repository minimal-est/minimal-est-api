package kr.minimalest.api.domain.publishing;

import jakarta.persistence.*;
import kr.minimalest.api.domain.access.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

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
    @AttributeOverride(
            name = "value",
            column = @Column(name = "pen_name", nullable = false, unique = true)
    )
    private PenName penName;

    @Embedded
    @AttributeOverride(
            name = "url",
            column = @Column(name = "profile_url", nullable = false)
    )
    private Profile profile;

    private static final int PEN_NAME_MIN_LENGTH = 3;
    private static final int PEN_NAME_MAX_LENGTH = 20;
    private static final Pattern PEN_NAME_PATTERN = Pattern.compile("^[\\p{L}\\d\\-_]{3,20}$");

    private static void validatePenName(PenName penName) {
        String value = penName.value().trim();

        Assert.isTrue(
                value.length() >= PEN_NAME_MIN_LENGTH && value.length() <= PEN_NAME_MAX_LENGTH,
                "penName은 " + PEN_NAME_MIN_LENGTH + "자 이상 " + PEN_NAME_MAX_LENGTH + "자 이하여야 합니다."
        );

        Assert.isTrue(
                PEN_NAME_PATTERN.matcher(value).matches(),
                "penName은 영어, 한글, 숫자, -, _만 사용할 수 있습니다."
        );
    }

    static Author create(UserId userId, PenName penName) {
        validatePenName(penName);

        Author author = new Author();
        author.id = AuthorId.generate();
        author.userId = userId;
        author.penName = penName;
        author.profile = Profile.random(userId.id());
        return author;
    }

    public void updateProfileUrl(String profileImageUrl) {
        this.profile = new Profile(profileImageUrl);
    }
}
