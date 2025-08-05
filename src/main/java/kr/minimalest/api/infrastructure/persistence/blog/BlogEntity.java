package kr.minimalest.api.infrastructure.persistence.blog;

import jakarta.persistence.*;
import kr.minimalest.api.infrastructure.persistence.user.UserEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "blogs")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blog_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(name = "blog_uuid", nullable = false, unique = true, length = 36)
    private String blogUUID;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
