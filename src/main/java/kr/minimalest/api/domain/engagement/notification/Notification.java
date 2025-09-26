package kr.minimalest.api.domain.engagement.notification;

import jakarta.persistence.*;
import kr.minimalest.api.domain.access.UserId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @EmbeddedId
    private NotificationId notificationId;

    @Embedded
    @AttributeOverride(
            name = "id",
            column = @Column(name = "target_user_id", nullable = false)
    )
    private UserId targetUserId;

    @Enumerated(EnumType.STRING)
    private NotificationState notificationState = NotificationState.UNREAD;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "expires_at", nullable = true)
    private LocalDateTime expiresAt;

    public static Notification issue(UserId targetUserId) {
        return new Notification(
                NotificationId.generate(),
                targetUserId,
                NotificationState.UNREAD,
                LocalDateTime.now(),
                LocalDateTime.MAX
        );
    }

    public void markRead() {
        if (this.notificationState != NotificationState.READ) {
            this.notificationState = NotificationState.READ;
        }
    }
}
