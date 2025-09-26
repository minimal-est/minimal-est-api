package kr.minimalest.api.domain.access;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "roles",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_type"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role {

    @EmbeddedId
    private RoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private RoleType roleType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    static Role ofDefault() {
        return new Role(RoleId.generate(), null, RoleType.USER, LocalDateTime.now());
    }

    static Role ofUser() {
        return new Role(RoleId.generate(), null, RoleType.USER, LocalDateTime.now());
    }

    static Role ofAdmin() {
        return new Role(RoleId.generate(), null, RoleType.ADMIN, LocalDateTime.now());
    }

    static Role of(RoleType type) {
        return new Role(RoleId.generate(), null, type, LocalDateTime.now());
    }

    void setUser(User user) {
        this.user = user;
    }
}
