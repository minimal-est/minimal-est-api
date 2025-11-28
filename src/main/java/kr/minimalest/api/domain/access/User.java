package kr.minimalest.api.domain.access;

import jakarta.persistence.*;
import kr.minimalest.api.domain.AggregateRoot;
import kr.minimalest.api.domain.access.event.UserSignedUpEvent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends AggregateRoot {

    @EmbeddedId
    private UserId id;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "email", nullable = false, unique = true, length = 50)
    )
    private Email email;

    @Embedded
    @AttributeOverride(
            name = "value",
            // oauth2일 경우 null 가능
            column = @Column(name = "password", nullable = true, unique = false)
    )
    private Password password;

    @Column(name = "email_verified")
    private boolean emailVerified = false;

    @Column(name = "oauth2_id")
    private String oauth2Id;

    @Column(name = "oauth2_provider")
    private String oauth2Provider;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static User signUp(Email email, Password password) {
        User u = new User(
                UserId.generate(),
                email,
                password,
                false,
                null,
                null,
                new HashSet<>(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        u.assignRole(Role.ofDefault());
        u.registerEvent(UserSignedUpEvent.of(u.getId(), u.getEmail()));
        return u;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public void setOAuth2(String oauth2Id, String oauth2Provider) {
        this.oauth2Id = oauth2Id;
        this.oauth2Provider = oauth2Provider;
        verifyEmail();
    }

    public boolean assignAdmin() {
        Role admin = Role.ofAdmin();
        if (!hasRole(admin.getRoleType())) {
            assignRole(admin);
            return true;
        }
        return false;
    }

    private void assignRole(Role role) {
        role.setUser(this);
        roles.add(role);
    }

    public boolean revokeRole(RoleType roleType) {
        for (Role role : roles) {
            if (role.getRoleType().equals(roleType)) {
                roles.remove(role);
                return true;
            }
        }
        return false;
    }

    public List<RoleType> getRoleTypes() {
        return roles.stream().map(Role::getRoleType).toList();
    }

    public boolean hasRole(RoleType roleType) {
        return roles.stream().anyMatch(role -> role.getRoleType() == roleType);
    }
}
